package cool.compiler;

import cool.compiler.*;
import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {
    Scope current_scope = null;


    @Override
    public TypeSymbol visit(ASTId astId) {
        Symbol sym = current_scope.lookup(astId.token.getText(), "type");
        if (sym == null) {
            SymbolTable.error(astId.ctx, astId.token, "Undefined identifier " + astId.token.getText());
        }
        return null;
    }

    @Override
    public TypeSymbol visit(ASTInt astInt) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTBool astBool) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTString astString) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTExplicitCall astExplicitCall) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTMultDiv astMultDiv) {
        return null;
    }
    // String evaluate_type(ASTExpression) {
    //
    // }
    @Override
    public TypeSymbol visit(ASTPlusMinus astPlusMinus) {
        // left insanceof ASTString
        // left si right sunt ASTInt, ASTId, ASTPlusMinus, ASTMultDiv


        return null;
    }

    @Override
    public TypeSymbol visit(ASTRelational astRelational) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTAssign astAssign) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTImplicitCall astImplicitCall) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTFormal astFormal) {
        String id = astFormal.id.token.getText();
        String type = astFormal.type.getText();
        TypeSymbol class_scope = (TypeSymbol) current_scope.getParent();

        TypeSymbol formal_type = (TypeSymbol) SymbolTable.globals.lookup(type, "");

        if (type.compareTo("SELF_TYPE") != 0 && formal_type == null) {
            SymbolTable.error(astFormal.context, astFormal.type, "Method " +
                    ((FunctionSymbol) current_scope).getName() + " of class " + class_scope.getName() + " has formal parameter " +
                    id + " with undefined type " + type);
            return null;
        }
        if (astFormal.id.sym != null)
            astFormal.id.sym.setType(formal_type);

        return null;
    }

    @Override
    public TypeSymbol visit(ASTFormalInit astFormalInit) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTClass astClass) {
        TypeSymbol sym = (TypeSymbol) current_scope.lookup(astClass.type.getText(), "sym");
        String parent_type = sym.getTypeParent();
        if (parent_type != null) {

            if (current_scope.lookup(parent_type, "sym") == null) {
                SymbolTable.error(astClass.context, astClass.start, "Class " + astClass.type.getText() + " has undefined parent");
            }

            TypeSymbol parent_sym = (TypeSymbol) current_scope.lookup(parent_type, "sym");
            while (parent_sym != null) {
                if (parent_sym.getName().compareTo(sym.getName()) == 0) {
                    SymbolTable.error(astClass.context, astClass.start, "Inheritance cycle for class " + astClass.type.getText());
                    break;
                }
                parent_type = parent_sym.getTypeParent();
                if (parent_type != null) {
                    parent_sym = (TypeSymbol) current_scope.lookup(parent_type, "sym");
                } else {
                    break;
                }
            }
        }
        current_scope = (TypeSymbol) astClass.sym;
        for (ASTFeature f : astClass.features) {
            f.accept(this);
            //if (type == null) {
               // break;
          //  }
        }
        current_scope = SymbolTable.globals;

        return (TypeSymbol) astClass.sym;
    }

    @Override
    public TypeSymbol visit(ASTMethod astMethod) {

        FunctionSymbol sym = (FunctionSymbol) astMethod.id.sym;

        if (sym == null) {
            return null;
        }
        String type = sym.getReturnType();
        String id = sym.getName();

        TypeSymbol class_scope = (TypeSymbol) current_scope;

        current_scope = sym;
        for (ASTFormal f : astMethod.formals) {
            f.accept(this);
        }
        current_scope = class_scope;

        String parent_type = ((TypeSymbol)current_scope).getTypeParent();
        TypeSymbol parent_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "sym");
        Map<String, Symbol> method_symbols_table = sym.getSymbolTable();



        //System.out.println("Class " + ((TypeSymbol) current_scope).getName()+ " method " + id);
        while (parent_sym != null) {
            Symbol overriden_method = parent_sym.lookup(astMethod.id.token.getText(), "methodSymbol");
            if (overriden_method != null) {
                Map<String, Symbol> overriden_method_symbols_table = ((FunctionSymbol) overriden_method).getSymbolTable();

                //System.out.println("Method: " + astMethod.id.token.getText() + " size " + method_symbols_table.size() + " size " +
                       // overriden_method_symbols_table.size());
                if (method_symbols_table.size() != overriden_method_symbols_table.size()) {
                    SymbolTable.error(astMethod.context, astMethod.id.token, "Class " + ((TypeSymbol)current_scope).getName() + " overrides method " +
                            astMethod.id.token.getText() + " with different number of formal parameters");
                    return null;
                } else {
                    // check return type
                    String method_return_type = sym.getReturnType();
                    String overriden_method_return_type = ((FunctionSymbol) overriden_method).getReturnType();
                    if (method_return_type.compareTo(overriden_method_return_type) != 0) {
                        SymbolTable.error(astMethod.context, astMethod.type, "Class " + ((TypeSymbol)current_scope).getName() + " overrides method " +
                                astMethod.id.token.getText() + " but changes return type from " +
                                overriden_method_return_type + " to " + method_return_type);
                        return null;
                    }

                    // extract formals from the two methods, in order
                    Set<String> method_keys = method_symbols_table.keySet();
                    Set<String> overriden_method_keys = overriden_method_symbols_table.keySet();

                    ArrayList<Symbol> method_symbols = new ArrayList<>();
                    ArrayList<Symbol> overriden_method_symbols = new ArrayList<>();

                    for (String method_key : method_keys) {
                        Symbol method_symbol = method_symbols_table.get(method_key);
                        method_symbols.add(method_symbol);
                    }
                    for (String method_key : overriden_method_keys) {
                      //  System.out.println(method_key);
                        Symbol overriden_method_symbol = overriden_method_symbols_table.get(method_key);
                        overriden_method_symbols.add(overriden_method_symbol);
                    }
                  //  System.out.println("Array size " +method_symbols.size() + " size " +  overriden_method_symbols.size());
                    // compare formals
                    for (int i = 0; i < method_symbols.size(); i++) {
                        Symbol method_formal = method_symbols.get(i);
                        Symbol overriden_formal = overriden_method_symbols.get(i);
                        if (method_formal.getType().getName().compareTo(overriden_formal.getType().getName()) != 0) {
                            ASTFormal formal = astMethod.formals.get(i);
                            SymbolTable.error(astMethod.context, formal.type, "Class " + ((TypeSymbol)current_scope).getName() + " overrides method " +
                                    astMethod.id.token.getText() + " but changes type of formal parameter " +
                                    method_formal.getName() + " from " + overriden_formal.getType().getName() + " to " +
                                    method_formal.getType().getName());
                            return null;
                        }
                    }
                }

                return null;
            }
            parent_type = parent_sym.getTypeParent();
            if (parent_type == null) {
                break;
            }
            parent_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "sym");
        }

        if (SymbolTable.globals.lookup(type, "") == null) {
            SymbolTable.error(astMethod.context, astMethod.id.token, "Class " + ((TypeSymbol)current_scope).getName() +
                    " has method " + id + "  with undefined return type " + type);
            return null;
        }



        //FunctionSymbol sym = (FunctionSymbol) current_scope.lookup(id, "methodSymbol");

        return null;
    }

    @Override
    public TypeSymbol visit(ASTWhile astWhile) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTBlock astBlock) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTLet astLet) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTCase astCase) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTNew astNew) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTIsVoid astIsVoid) {
        return null;
    }


    @Override
    public TypeSymbol visit(ASTComplement astComplement) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTNot astNot) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTSelf astSelf) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTProg astProg) {
        current_scope = SymbolTable.globals;

        for (var cls: astProg.classes) {
            cls.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(ASTIf astIf) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTMember astMember) {
        TypeSymbol sym = (TypeSymbol) current_scope.lookup(astMember.type.getText(), "");
        if (sym == null) {
            SymbolTable.error(astMember.context, astMember.type, "Class " + ((TypeSymbol)current_scope).getName() + " has attribute " +
                    astMember.id.token.getText() + " with undefined type " + astMember.type.getText());
            return null;
        }
        String parent_type = ((TypeSymbol)current_scope).getTypeParent();
        TypeSymbol parent_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "sym");
        while (parent_sym != null) {

            if (parent_sym.lookup(astMember.id.token.getText(), "memberSymbol") != null) {
                SymbolTable.error(astMember.context, astMember.id.token, "Class " + ((TypeSymbol)current_scope).getName() + " redefines inherited attribute " +
                        astMember.id.token.getText());
                return null;
            }
            parent_type = parent_sym.getTypeParent();
            if (parent_type == null) {
                break;
            }
            parent_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "sym");
        }

        if (astMember.expr != null) {
            TypeSymbol expr_type_sym = astMember.expr.accept(this);
            TypeSymbol id_type_sym = (TypeSymbol) current_scope.lookup(astMember.type.getText(), "");

            if (expr_type_sym == null) {
                return null;
            }
            boolean compatible_types = false;
            if (expr_type_sym.getName().compareTo(id_type_sym.getName()) != 0) {
                String id_type_parent = id_type_sym.getTypeParent();

                while (id_type_parent != null) {
                    if (id_type_parent.compareTo(expr_type_sym.getName()) == 0) {
                        compatible_types = true;
                        break;
                    }
                    id_type_sym = (TypeSymbol) current_scope.lookup(id_type_parent, "");
                    id_type_parent = id_type_sym.getTypeParent();
                }

            } else {
                compatible_types = true;
            }
            if (!compatible_types) {
                SymbolTable.error(astMember.context, astMember.expr.token, "Type " + expr_type_sym.getName() + " of initialization expression of attribute " +
                        astMember.id.token.getText() + " is incompatible with declared type " + id_type_sym.getName());
                return null;
            }

        }

        return sym;
    }

    @Override
    public TypeSymbol visit(ASTCaseBranch astCaseBranch) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTType astType) {
        return null;
    }
};