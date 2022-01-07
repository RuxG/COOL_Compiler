package cool.compiler;

import cool.compiler.*;
import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.Type;
import java.util.*;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {
    Scope current_scope = null;


    @Override
    public TypeSymbol visit(ASTId astId) {
        Symbol sym = current_scope.lookup(astId.token.getText(), "memberSymbol");
        if (sym == null) {
            SymbolTable.error(astId.ctx, astId.token, "Undefined identifier " + astId.token.getText());
            return null;
        }
        TypeSymbol type_sym = sym.getType();
        return type_sym;
    }

    @Override
    public TypeSymbol visit(ASTInt astInt) {
        return TypeSymbol.INT;
    }
    @Override
    public TypeSymbol visit(ASTBool astBool) {
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(ASTString astString) {
        return TypeSymbol.STRING;
    }

    @Override
    public TypeSymbol visit(ASTExplicitCall astExplicitCall) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTMultDiv astMultDiv) {
        TypeSymbol left_type_sym = astMultDiv.left_expr.accept(this);
        TypeSymbol right_type_sym = astMultDiv.right_expr.accept(this);

        boolean error = false;
        if (right_type_sym != null && right_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astMultDiv.ctx, astMultDiv.right_expr.token, "Operand of " +
                    astMultDiv.type.getText() +  " has type " + right_type_sym.getName() +  " instead of Int");
            error = true;
        }
        if (left_type_sym != null && left_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astMultDiv.ctx, astMultDiv.left_expr.token, "Operand of " +
                    astMultDiv.type.getText() +  " has type " + left_type_sym.getName() +  " instead of Int");
            error = true;
        }

        if (error) return null;
        return TypeSymbol.INT;
    }
    // String evaluate_type(ASTExpression) {
    //
    // }
    @Override
    public TypeSymbol visit(ASTPlusMinus astPlusMinus) {
        TypeSymbol left_type_sym = astPlusMinus.left_expr.accept(this);
        TypeSymbol right_type_sym = astPlusMinus.right_expr.accept(this);

        boolean error = false;
        if (right_type_sym != null && right_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astPlusMinus.ctx, astPlusMinus.right_expr.token, "Operand of " +
                    astPlusMinus.op.getText() + " has type " + right_type_sym.getName() + " instead of Int");
            error = true;
        }
        if (left_type_sym != null && left_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astPlusMinus.ctx, astPlusMinus.left_expr.token, "Operand of " +
                    astPlusMinus.op.getText() +  " has type " + left_type_sym.getName() +  " instead of Int");
             error = true;
        }

        if (error == true) return null;
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(ASTRelational astRelational) {
        TypeSymbol left_expression_type = astRelational.left_expr.accept(this);
        TypeSymbol right_expression_type = astRelational.right_expr.accept(this);

        if (astRelational.op.getText().compareTo("=") != 0) {
            if (left_expression_type != null && left_expression_type.getName().compareTo("Int") != 0) {
                SymbolTable.error(astRelational.ctx, astRelational.left_expr.token, "Operand of " +
                        astRelational.op.getText() +  " has type " + left_expression_type.getName() +  " instead of Int");
            }

            if (right_expression_type != null && right_expression_type.getName().compareTo("Int") != 0) {
                SymbolTable.error(astRelational.ctx, astRelational.right_expr.token, "Operand of " +
                        astRelational.op.getText() +  " has type " + right_expression_type.getName() +  " instead of Int");
            }
        } else if (left_expression_type != null && right_expression_type!= null) {
            String left_type = left_expression_type.getName();
            String right_type = right_expression_type.getName();
            if ((left_type.compareTo("Int") == 0 && right_type.compareTo("Int") != 0) ||
                    (left_type.compareTo("Int") != 0 && right_type.compareTo("Int") == 0) ||
                    (left_type.compareTo("String") != 0 && right_type.compareTo("String") == 0) ||
                    (left_type.compareTo("String") == 0 && right_type.compareTo("String") != 0) ||
                    (left_type.compareTo("Bool") == 0 && right_type.compareTo("Bool") != 0) ||
                    (left_type.compareTo("Bool") != 0 && right_type.compareTo("Bool") == 0)) {
                SymbolTable.error(astRelational.ctx, astRelational.op, "Cannot compare " +
                        left_type + " with " + right_type);
            }
        }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(ASTAssign astAssign) {
        if (astAssign.id.token.getText().compareTo("self") == 0) {
            SymbolTable.error(astAssign.ctx, astAssign.id.token, "Cannot assign to self");
            return null;
        }
        TypeSymbol id_type = astAssign.id.accept(this);



        // x metoda, x membru
        // x <- 2

        // x metoda
        // x <- 2
        // x + 2

        // x membru
        // x <- 2


        Symbol id_sym = current_scope.lookup(astAssign.id.token.getText(), "");
        astAssign.id.sym = id_sym;
        astAssign.id.sym.setType(id_type);

        TypeSymbol init_type = astAssign.expr.accept(this);
        if (init_type == null) {
            return id_type;
        }

        boolean compatible_type = false;
        if (id_type.getName().compareTo(init_type.getName()) != 0) {
            String parent_type = init_type.getTypeParent();
            if (parent_type != null) {
                TypeSymbol parent_type_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "");
                while (parent_type_sym != null) {
                    if (parent_type_sym.getName().compareTo(id_type.getName()) == 0) {
                        compatible_type = true;
                        break;
                    }
                    parent_type = parent_type_sym.getTypeParent();
                    parent_type_sym = null;
                    if (parent_type != null) {
                        parent_type_sym = (TypeSymbol) current_scope.lookup(parent_type, "sym");
                    }
                }
            }
        } else {
            compatible_type = true;
        }
        if (!compatible_type) {
            SymbolTable.error(astAssign.ctx, astAssign.expr.token, "Type " + init_type.getName() +
                    " of assigned expression is incompatible with declared type " +
                    id_type.getName() + " of identifier " + astAssign.id.token.getText());
            return null;
        }
        return id_type.getType();
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

        if (astFormal.id.sym == null) {
            return null;
        }

        TypeSymbol formal_type = (TypeSymbol) SymbolTable.globals.lookup(type, "");

        if (type.compareTo("SELF_TYPE") != 0 && formal_type == null) {
            SymbolTable.error(astFormal.context, astFormal.type, "Method " +
                    ((FunctionSymbol) current_scope).getName() + " of class " + class_scope.getName() + " has formal parameter " +
                    id + " with undefined type " + type);
            return null;
        }
        astFormal.id.sym.setType(formal_type);

        return formal_type;
    }

    @Override
    public TypeSymbol visit(ASTFormalInit astFormalInit) {
        String id = astFormalInit.id.token.getText();
        String type = astFormalInit.type.getText();

        if (astFormalInit.id.sym == null) {
            return null;
        }

        TypeSymbol formal_type = (TypeSymbol) SymbolTable.globals.lookup(type, "");
        if (formal_type == null) {
            SymbolTable.error(astFormalInit.ctx, astFormalInit.type, "Let variable " + astFormalInit.id.token.getText() +
                    " has undefined type " + type);
            return null;
        }

        // trebuie cautat expresia de initializare in scope-ul care nu contine parametrul formal
        Scope scope = current_scope;
        current_scope = new FunctionSymbol(((FunctionSymbol)current_scope).getName(), scope.getParent());
        Map<String, Symbol> symbol_table = ((FunctionSymbol)scope).getSymbolTable();
        Symbol sym = symbol_table.get(astFormalInit.expr.token.getText());
        if (sym != null) {
            symbol_table.remove(sym.getName());
        }
        ((FunctionSymbol) current_scope).setSymbolTable(symbol_table);

        TypeSymbol init_type = astFormalInit.expr.accept(this);
        current_scope = scope;
        if (init_type == null) {
            return null;
        }

        boolean compatible_type = false;
        if (formal_type.getName().compareTo(init_type.getName()) != 0) {
            String parent_type = init_type.getTypeParent();
            if (parent_type != null) {
                TypeSymbol parent_type_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "");
                while (parent_type_sym != null) {
                    if (parent_type_sym.getName().compareTo(formal_type.getName()) == 0) {
                        compatible_type = true;
                        break;
                    }
                    parent_type = parent_type_sym.getTypeParent();
                    parent_type_sym = null;
                    if (parent_type != null) {
                        parent_type_sym = (TypeSymbol) current_scope.lookup(parent_type, "sym");
                    }
                }
            }
        } else {
            compatible_type = true;
        }

        if (!compatible_type) {
            SymbolTable.error(astFormalInit.ctx, astFormalInit.expr.token, "Type " + init_type.getName() +
                    " of initialization expression of identifier " +  astFormalInit.id.token.getText() +
                    " is incompatible with declared type " + astFormalInit.type.getText());
        }

        astFormalInit.id.sym.setType(formal_type);
        return formal_type;
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
        astMethod.body.accept(this);
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


        TypeSymbol return_type = (TypeSymbol) SymbolTable.globals.lookup(astMethod.type.getText(), "");
        //FunctionSymbol sym = (FunctionSymbol) current_scope.lookup(id, "methodSymbol");

        return return_type;
    }

    @Override
    public TypeSymbol visit(ASTWhile astWhile) {
        TypeSymbol cond_type = astWhile.cond_expr.accept(this);
        if (cond_type.getName().compareTo("Bool") != 0) {
            SymbolTable.error(astWhile.ctx, astWhile.cond_expr.token, "While condition has type " +
                    cond_type.getName() + " instead of Bool");
            return TypeSymbol.OBJECT;
        }
        astWhile.body.accept(this);
        return TypeSymbol.OBJECT;
    }

    @Override
    public TypeSymbol visit(ASTBlock astBlock) {
        return null;
    }

    @Override
    public TypeSymbol visit(ASTLet astLet) {
        Scope scope = current_scope;
        current_scope = (FunctionSymbol) astLet.sym;

        boolean error = false;

        for (ASTFormalInit f : astLet.formals) {
            if (f.accept(this) == null) {
                error = true;
            }
        }

        if (error) return null;

        TypeSymbol type = astLet.ex.accept(this);
        current_scope = scope;
        return type;
    }

    @Override
    public TypeSymbol visit(ASTCase astCase) {

        TypeSymbol type_sym = astCase.expr.accept(this);
        if (type_sym == null) {
            return null;
        }

        ArrayList<TypeSymbol> branches_types = new ArrayList<>();

        TypeSymbol type = null;

        for (ASTCaseBranch b : astCase.branches) {
            type = b.accept(this);
            if (type != null) branches_types.add(type);
        }

        // verify if all case branches have the same type
        boolean same_type = true;
        for (int i = 0; i < branches_types.size() - 1; i++) {
            if (branches_types.get(i).getName().compareTo(branches_types.get(i + 1).getName()) != 0) {
                same_type = false;
                break;
            }
        }
        if (same_type) return type;

        // build parent lists for each branch
        List<List<String>> parent_types = new ArrayList<>();

        int max_length = 0;
        int max_length_list_index = 0;
        for (int i = 0; i < branches_types.size(); i++) {
            int length = 0;
            parent_types.add(new ArrayList<>());
            while (true) {

                String parent_type = branches_types.get(i).getTypeParent();
                TypeSymbol parent_type_symbol = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "");
                if (parent_type_symbol == null) {
                    break;
                }
                length++;
                if (length > max_length) {
                    max_length = length;
                    max_length_list_index = i;
                }
                parent_types.get(i).add(parent_type);

                branches_types.remove(i);
                branches_types.add(i, parent_type_symbol);
            }
        }

        List<String> longest_list = parent_types.get(max_length_list_index);
        parent_types.remove(max_length_list_index);
        parent_types.add(0, longest_list);
        List<String> commons = new ArrayList<String>();
        commons.addAll(parent_types.get(0));
        for (ListIterator<List<String>> iter = parent_types.listIterator(1); iter.hasNext(); ) {
            commons.retainAll(iter.next());
        }
        String lowest_common_ancestor = null;
        TypeSymbol lowest_common_ancestor_type = null;
        if (commons.size() > 0) {
            lowest_common_ancestor = commons.get(0);
            lowest_common_ancestor_type = (TypeSymbol) SymbolTable.globals.lookup(lowest_common_ancestor, "");
        }
        return lowest_common_ancestor_type;

    }

    @Override
    public TypeSymbol visit(ASTNew astNew) {
        TypeSymbol type_sym = (TypeSymbol) SymbolTable.globals.lookup(astNew.type.getText(), "");
        if (type_sym == null) {
            SymbolTable.error(astNew.ctx, astNew.type, "new is used with undefined type " + astNew.type.getText());
            return null;
        }
        return type_sym;
    }

    @Override
    public TypeSymbol visit(ASTIsVoid astIsVoid) {
        TypeSymbol type = astIsVoid.expr.accept(this);
        return TypeSymbol.BOOL;
    }


    @Override
    public TypeSymbol visit(ASTComplement astComplement) {
        TypeSymbol type_sym = astComplement.expr.accept(this);

        if (type_sym != null && type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astComplement.ctx, astComplement.expr.token, "Operand of " +
                    astComplement.complement.getText() +  " has type " + type_sym.getName() +  " instead of Int");
        }
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(ASTNot astNot) {
        TypeSymbol type_sym = astNot.expr.accept(this);
        if (type_sym.getName().compareTo("Bool") != 0) {
            SymbolTable.error(astNot.ctx, astNot.expr.token, "Operand of not has type " +
                    type_sym.getName() + " instead of Bool");
        }
        return TypeSymbol.BOOL;
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
        Symbol member_sym = astMember.id.sym;
        if (member_sym == null) {
            return null;
        }

        TypeSymbol sym = (TypeSymbol) current_scope.lookup(astMember.type.getText(), "");
        if (sym == null) {
            SymbolTable.error(astMember.context, astMember.type, "Class " + ((TypeSymbol)current_scope).getName() + " has attribute " +
                    astMember.id.token.getText() + " with undefined type " + astMember.type.getText());
            return null;
        }
        astMember.id.sym.setType(sym);

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

        if (astCaseBranch.sym == null) {
            return  null;
        }

        String type = astCaseBranch.type.getText();

        if (type.compareTo("SELF_TYPE") == 0) {
            SymbolTable.error(astCaseBranch.ctx, astCaseBranch.type, "Case variable " +
                    astCaseBranch.id.token.getText() + " has illegal type SELF_TYPE");
            return null;
        }

        TypeSymbol type_sym = (TypeSymbol) SymbolTable.globals.lookup(type, "");
        if (type_sym == null) {
            SymbolTable.error(astCaseBranch.ctx, astCaseBranch.type, "Case variable " +
                    astCaseBranch.id.token.getText() + " has undefined type " + type);
            return null;
        }

        astCaseBranch.id.sym.setType(type_sym);


        Scope scope = current_scope;
        current_scope = (CaseSymbol) astCaseBranch.sym;
        current_scope.modify(astCaseBranch.id.sym);

        TypeSymbol case_branch_type = astCaseBranch.results.accept(this);

        current_scope = scope;
        return case_branch_type;
    }

    @Override
    public TypeSymbol visit(ASTType astType) {
        return null;
    }
};