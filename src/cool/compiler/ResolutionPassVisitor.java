package cool.compiler;

import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;

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

        TypeSymbol type_sym = (TypeSymbol) SymbolTable.globals.lookup(sym.getType(), "");
        if (type_sym == null && sym.getType() != null) {
            type_sym = (TypeSymbol) SymbolTable.globals.lookup(sym.getType(), "");
        }

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

        String method_id = astExplicitCall.id.token.getText();
        TypeSymbol caller_type = astExplicitCall.expr.accept(this);
        String method_class;

        if (caller_type == null) {
            return null;
        }

        FunctionSymbol method_symbol;
        Map<String, Symbol> symbols;
        String caller_name = ((TypeSymbol) caller_type).getName();

        // static dispatch
        if (astExplicitCall.type != null) {
            String static_type_name = astExplicitCall.type.getText();

            if (static_type_name.compareTo("SELF_TYPE") == 0) {
                SymbolTable.error(astExplicitCall.ctx, astExplicitCall.type, "Type of static dispatch cannot be SELF_TYPE");
                return null;
            }

            TypeSymbol static_type = (TypeSymbol) SymbolTable.globals.lookup(static_type_name, "");
            if (static_type == null) {
                SymbolTable.error(astExplicitCall.ctx, astExplicitCall.type, "Type " + astExplicitCall.type.getText() +
                        " of static dispatch is undefined");
                return null;
            }

            method_symbol = (FunctionSymbol) static_type.lookup(method_id, "methodSymbol");
            if (method_symbol == null) {
                SymbolTable.error(astExplicitCall.ctx, astExplicitCall.id.token, "Undefined method " +
                        astExplicitCall.id.token.getText() + " in class " + astExplicitCall.type.getText());
                return null;
            }

            boolean compatible_type = compatible_types(caller_type, caller_name, static_type_name);

            if (!compatible_type) {
                SymbolTable.error(astExplicitCall.ctx, astExplicitCall.type, "Type " + static_type_name + " of static dispatch is not a superclass of type " +
                        caller_name);
                return null;
            }
            method_class = static_type_name;

        } else {
             method_symbol = (FunctionSymbol) caller_type.lookup(method_id, "methodSymbol");
             if (method_symbol == null) {
                 SymbolTable.error(astExplicitCall.ctx, astExplicitCall.id.token, "Undefined method " +
                         method_id + " in class " + caller_name);
                 return null;
             }

            method_class = caller_name;
        }

        TypeSymbol return_type = (TypeSymbol) SymbolTable.globals.lookup(method_symbol.getReturnType(), "");

        symbols = method_symbol.getSymbolTable();
        if (symbols.size() != astExplicitCall.args.size()) {
            SymbolTable.error(astExplicitCall.ctx, astExplicitCall.id.token, "Method " + method_id +
                    " of class " + method_class + " is applied to wrong number of arguments");
            return return_type;
        }

        check_formals_arguments_compatibility(astExplicitCall.ctx, (FunctionSymbol) method_symbol, (ArrayList<ASTExpression>) astExplicitCall.args, method_id, method_class);
        
        return return_type;
    }

    @Override
    public TypeSymbol visit(ASTMultDiv astMultDiv) {
        TypeSymbol left_type_sym = astMultDiv.left_expr.accept(this);
        TypeSymbol right_type_sym = astMultDiv.right_expr.accept(this);

        if (right_type_sym != null && right_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astMultDiv.ctx, astMultDiv.right_expr.token, "Operand of " +
                    astMultDiv.type.getText() +  " has type " + right_type_sym.getName() +  " instead of Int");

        } else if (left_type_sym != null && left_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astMultDiv.ctx, astMultDiv.left_expr.token, "Operand of " +
                    astMultDiv.type.getText() +  " has type " + left_type_sym.getName() +  " instead of Int");

        }

        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(ASTPlusMinus astPlusMinus) {
        TypeSymbol left_type_sym = astPlusMinus.left_expr.accept(this);
        TypeSymbol right_type_sym = astPlusMinus.right_expr.accept(this);

        if (right_type_sym != null && right_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astPlusMinus.ctx, astPlusMinus.right_expr.token, "Operand of " +
                    astPlusMinus.op.getText() + " has type " + right_type_sym.getName() + " instead of Int");

        } else if (left_type_sym != null && left_type_sym.getName().compareTo("Int") != 0) {
            SymbolTable.error(astPlusMinus.ctx, astPlusMinus.left_expr.token, "Operand of " +
                    astPlusMinus.op.getText() +  " has type " + left_type_sym.getName() +  " instead of Int");
        }

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
        String id = astAssign.id.token.getText();
        if (id.compareTo("self") == 0) {
            SymbolTable.error(astAssign.ctx, astAssign.id.token, "Cannot assign to self");
            return null;
        }

        TypeSymbol id_type_sym = astAssign.id.accept(this);
        String id_type = null;
        if (id_type_sym != null) {
            id_type = id_type_sym.getName();
        }

        Symbol id_sym = current_scope.lookup(id, "");
        if (id_sym == null) {
            return null;
        }

        astAssign.id.sym = id_sym;
        astAssign.id.sym.setType(id_type);

        TypeSymbol init_type = astAssign.expr.accept(this);
        if (init_type == null) {
            return null;
        }

        boolean compatible_type = compatible_types(init_type, init_type.getName(), id_type);
        if (!compatible_type) {
            SymbolTable.error(astAssign.ctx, astAssign.expr.token, "Type " + init_type.getName() +
                    " of assigned expression is incompatible with declared type " +
                    id_type + " of identifier " + id);
            return null;
        }

        TypeSymbol type = (TypeSymbol) SymbolTable.globals.lookup(init_type.getType(), "");
        return type;
    }

    @Override
    public TypeSymbol visit(ASTImplicitCall astImplicitCall) {

        Scope scope = current_scope;
        while (! (scope instanceof TypeSymbol)) {
            scope = scope.getParent();
        }
        String class_name = ((TypeSymbol) scope).getName();

        String method_id = astImplicitCall.id.token.getText();
        Symbol method_symbol = current_scope.lookup(method_id, "methodSymbol");
        if (method_symbol == null) {
            SymbolTable.error(astImplicitCall.ctx, astImplicitCall.id.token, "Undefined method " +
                    method_id + " in class " + ((TypeSymbol)scope).getName());
            return null;
        }


        TypeSymbol return_type = (TypeSymbol) SymbolTable.globals.lookup(((FunctionSymbol)method_symbol).getReturnType(), "");

        Map<String, Symbol> symbols = ((FunctionSymbol) method_symbol).getSymbolTable();
        if (symbols.size() != astImplicitCall.args.size()) {
            SymbolTable.error(astImplicitCall.ctx, astImplicitCall.id.token, "Method " + method_id +
                    " of class " + class_name + " is applied to wrong number of arguments");
            return return_type;
        }

        check_formals_arguments_compatibility(astImplicitCall.ctx, (FunctionSymbol) method_symbol, (ArrayList<ASTExpression>) astImplicitCall.args,method_id, class_name);

        return return_type;
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

       /* if (type.compareTo("SELF_TYPE") != 0 && formal_type == null) {
            SymbolTable.error(astFormal.context, astFormal.type, "Method " +
                    ((FunctionSymbol) current_scope).getName() + " of class " + class_scope.getName() + " has formal parameter " +
                    id + " with undefined type " + type);
            return null;
        }*/
        if (formal_type == null) {
            SymbolTable.error(astFormal.context, astFormal.type, "Method " +
                    ((FunctionSymbol) current_scope).getName() + " of class " + class_scope.getName() + " has formal parameter " +
                    id + " with undefined type " + type);
            return null;
        }
        astFormal.id.sym.setType(formal_type.getName());

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

        if (astFormalInit.expr == null) {
            return formal_type;
        }

        // check if initialization expression is defined before let variable
        Map<String, Symbol> symbol_table = ((FunctionSymbol)current_scope).getSymbolTable();

        Set<String> let_id_keys = symbol_table.keySet();
        int let_id_index = -1;
        int init_expression_index = -1;
        int i = 0;
        for (String let_id_key : let_id_keys) {
            Symbol id_symbol = symbol_table.get(let_id_key);
            if (id_symbol.getName().compareTo(id) == 0) {
                let_id_index = i;
            }
            if (id_symbol.getName().compareTo(astFormalInit.expr.token.getText()) == 0) {
                init_expression_index = i;
            }
            i++;
        }

        if (init_expression_index != -1 && (let_id_index <= init_expression_index) ) {
            SymbolTable.error(astFormalInit.expr.ctx, astFormalInit.expr.token, "Undefined identifier " +
                    astFormalInit.expr.token.getText());
            return formal_type;
        }

        TypeSymbol init_type = astFormalInit.expr.accept(this);
        if (init_type == null) {
            return formal_type;
        }

        // check if let variable and initialization expression have compatible types
        boolean compatible_type = compatible_types(init_type, init_type.getName(), formal_type.getName());

        if (!compatible_type) {
            SymbolTable.error(astFormalInit.ctx, astFormalInit.expr.token, "Type " + init_type.getName() +
                    " of initialization expression of identifier " +  astFormalInit.id.token.getText() +
                    " is incompatible with declared type " + astFormalInit.type.getText());
        }

        astFormalInit.id.sym.setType(formal_type.getName());
        return formal_type;
    }

    @Override
    public TypeSymbol visit(ASTClass astClass) {
        TypeSymbol sym = (TypeSymbol) current_scope.lookup(astClass.type.getText(), "sym");
        String parent_type = sym.getTypeParent();
        if (parent_type != null) {

            if (current_scope.lookup(parent_type, "sym") == null) {
                SymbolTable.error(astClass.context, astClass.parent_type, "Class " + astClass.type.getText() + " has undefined parent " + astClass.parent_type.getText());
            }

            TypeSymbol parent_sym = (TypeSymbol) current_scope.lookup(parent_type, "sym");
            while (parent_sym != null) {
                if (parent_sym.getName().compareTo(sym.getName()) == 0) {
                    SymbolTable.error(astClass.context, astClass.type, "Inheritance cycle for class " + astClass.type.getText());
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
        TypeSymbol body_type = astMethod.body.accept(this);
        current_scope = class_scope;

        String parent_type = ((TypeSymbol)current_scope).getTypeParent();
        TypeSymbol parent_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "");
        Map<String, Symbol> method_symbols_table = sym.getSymbolTable();

        while (parent_sym != null) {
            Symbol overriden_method = parent_sym.lookup(astMethod.id.token.getText(), "methodSymbol");
            if (overriden_method != null) {
                Map<String, Symbol> overriden_method_symbols_table = ((FunctionSymbol) overriden_method).getSymbolTable();

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
                        Symbol overriden_method_symbol = overriden_method_symbols_table.get(method_key);
                        overriden_method_symbols.add(overriden_method_symbol);
                    }

                    for (int i = 0; i < method_symbols.size(); i++) {
                        Symbol method_formal = method_symbols.get(i);
                        Symbol overriden_formal = overriden_method_symbols.get(i);
                        if (method_formal.getType().compareTo(overriden_formal.getType()) != 0) {
                            ASTFormal formal = astMethod.formals.get(i);
                            SymbolTable.error(astMethod.context, formal.type, "Class " + ((TypeSymbol)current_scope).getName() + " overrides method " +
                                    astMethod.id.token.getText() + " but changes type of formal parameter " +
                                    method_formal.getName() + " from " + overriden_formal.getType() + " to " +
                                    method_formal.getType());
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
        if (body_type == null) {
            return return_type;
        }

        String body_type_name = body_type.getName();

        boolean compatible_types;
        if (return_type.getName().compareTo("Object") == 0) {
            compatible_types = true;
        } else {
            compatible_types = compatible_types(body_type, body_type_name, return_type.getName());
        }

        if (!compatible_types) {
            SymbolTable.error(astMethod.context, astMethod.body.token, "Type " +
                    body_type_name + " of the body of method " + astMethod.id.token.getText() +
                    " is incompatible with declared return type " + astMethod.type.getText());
            return null;
        }

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
        TypeSymbol type = null;
        for (ASTExpression e : astBlock.exprs) {
            type = e.accept(this);
        }
        return type;
    }

    @Override
    public TypeSymbol visit(ASTLet astLet) {
        Scope scope = current_scope;
        current_scope = (FunctionSymbol) astLet.sym;

        for (ASTFormalInit f : astLet.formals) {
            f.accept(this);
        }

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

        TypeSymbol type;

        for (ASTCaseBranch b : astCase.branches) {
            type = b.accept(this);
            if (type != null) branches_types.add(type);
        }

        TypeSymbol lowest_common_ancestor = lowest_common_ancestor(branches_types);

        return lowest_common_ancestor;

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
        astIsVoid.expr.accept(this);
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
            SymbolTable.error(astNot.ctx, astNot.expr.token, "Operand of not has type " + type_sym.getName() + " instead of Bool");
        }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(ASTSelf astSelf) {
        Scope scope = current_scope;
        while (! (scope instanceof TypeSymbol)) {
            scope = scope.getParent();
        }
        return (TypeSymbol) scope;
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
        TypeSymbol cond_type = astIf.cond.accept(this);
        if (cond_type == null) {
            return null;
        }
        if (cond_type.getName().compareTo("Bool") != 0) {
            SymbolTable.error(astIf.ctx, astIf.cond.ctx.start, "If condition has type " +
                    cond_type.getName() + " instead of Bool");
        }

        TypeSymbol then_branch_type = astIf.thenBranch.accept(this);
        TypeSymbol else_branch_type = astIf.elseBranch.accept(this);

        if (then_branch_type == null || else_branch_type == null) return null;

        ArrayList<TypeSymbol> branches_types = new ArrayList<>();
        branches_types.add(then_branch_type);
        branches_types.add(else_branch_type);

        TypeSymbol lowest_common_ancestor = lowest_common_ancestor(branches_types);

        return lowest_common_ancestor;
    }

    @Override
    public TypeSymbol visit(ASTMember astMember) {
        Symbol member_sym = astMember.id.sym;
        if (member_sym == null) {
            return null;
        }

        TypeSymbol sym = (TypeSymbol) SymbolTable.globals.lookup(astMember.type.getText(), "");

        if (sym == null) {
            SymbolTable.error(astMember.context, astMember.type, "Class " + ((TypeSymbol)current_scope).getName() + " has attribute " +
                    astMember.id.token.getText() + " with undefined type " + astMember.type.getText());
            return null;
        }
        astMember.id.sym.setType(sym.getName());

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
            TypeSymbol id_type_sym = (TypeSymbol) SymbolTable.globals.lookup(astMember.id.sym.getType(), "");

            if (expr_type_sym == null) {
                return null;
            }

            if (id_type_sym == null) {
                return null;
            }

            boolean compatible_types = compatible_types(expr_type_sym, expr_type_sym.getName(), id_type_sym.getName());

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

        astCaseBranch.id.sym.setType(type_sym.getName());


        Scope scope = current_scope;
        current_scope = (FunctionSymbol) astCaseBranch.sym;
        current_scope.modify(astCaseBranch.id.sym);

        TypeSymbol case_branch_type = astCaseBranch.results.accept(this);

        current_scope = scope;
        return case_branch_type;
    }

    @Override
    public TypeSymbol visit(ASTType astType) {
        return (TypeSymbol) SymbolTable.globals.lookup(astType.start.getText(), "");
    }

    TypeSymbol lowest_common_ancestor(List<TypeSymbol> types) {

        boolean same_type = true;
        for (int i = 0; i < types.size() - 1; i++) {
            if (types.get(i).getName().compareTo(types.get(i + 1).getName()) != 0) {
                same_type = false;
                break;
            }
        }
        if (same_type) return types.get(0);

        List<List<String>> parent_types = new ArrayList<>();

        int max_length = 0;
        int max_length_list_index = 0;

        for (int i = 0; i < types.size(); i++) {
            int length = 0;
            parent_types.add(new ArrayList<>());
            while (true) {

                String parent_type = types.get(i).getTypeParent();
                TypeSymbol parent_type_symbol = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "");
                if (parent_type_symbol == null) {
                    if (parent_types.get(i).size() == 0) {
                        parent_types.get(i).add(types.get(i).getName());
                    }
                    break;
                }
                length++;
                if (length > max_length) {
                    max_length = length;
                    max_length_list_index = i;
                }
                parent_types.get(i).add(parent_type);

                types.remove(i);
                types.add(i, parent_type_symbol);
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

        String lowest_common_ancestor;
        TypeSymbol lowest_common_ancestor_type = null;
        if (commons.size() > 0) {
            lowest_common_ancestor = commons.get(0);
            lowest_common_ancestor_type = (TypeSymbol) SymbolTable.globals.lookup(lowest_common_ancestor, "");
        }
        return lowest_common_ancestor_type;
    }

    boolean compatible_types(Symbol type1_sym, String type1, String type2) {
        boolean compatible_type = false;

        if (type2.compareTo("Int") == 0  || type2.compareTo("Bool") == 0 ||
                type2.compareTo("String") == 0) {
            if (type2.compareTo(type1) == 0) {
                compatible_type = true;
            }
        } else {
            if (type2.compareTo(type1) != 0) {

                String parent_type = ((TypeSymbol) type1_sym).getTypeParent();
                if (parent_type != null) {
                    TypeSymbol parent_type_sym = (TypeSymbol) SymbolTable.globals.lookup(parent_type, "");
                    while (parent_type_sym != null) {
                        if (parent_type_sym.getName().compareTo(type2) == 0) {
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
        }
        return compatible_type;
    }

    void check_formals_arguments_compatibility(ParserRuleContext ctx, FunctionSymbol method_symbol, ArrayList<ASTExpression> args, String method_id,
                                               String class_name) {

        Scope temp_scope = current_scope;
        current_scope = (Scope) method_symbol;

        Map<String, Symbol> symbols = ((FunctionSymbol) method_symbol).getSymbolTable();
        Set<String> formal_keys = symbols.keySet();

        int i = 0;
        for (String formal_key : formal_keys) {

            Symbol formal_symbol = symbols.get(formal_key);
            TypeSymbol formal_type = (TypeSymbol) SymbolTable.globals.lookup(formal_symbol.getType(), "");
            if (formal_type == null) {
                break;
            }

            TypeSymbol argument_symbol = args.get(i).accept(this);
            boolean compatible_types = compatible_types(argument_symbol, argument_symbol.getName(), formal_type.getName());

            if (!compatible_types) {
                SymbolTable.error(ctx, args.get(i).token, "In call to method " +
                        method_id + " of class " + class_name + ", actual type " +
                        argument_symbol.getName() + " of formal parameter " +
                        formal_key + " is incompatible with declared type " +
                        formal_type.getName());

                break;
            }
            i++;
        }

        current_scope = temp_scope;

        return;
    }

};