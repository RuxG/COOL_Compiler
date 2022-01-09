package cool.compiler;

import cool.structures.*;

public class DefinitionPassVisitor implements ASTVisitor<Void> {
    Scope currentScope = null;

    @Override
    public Void visit(ASTId astId) {

        return null;
    }

    @Override
    public Void visit(ASTInt astInt) {
        return null;
    }

    @Override
    public Void visit(ASTBool astBool) {
        return null;
    }

    @Override
    public Void visit(ASTString astString) {
        return null;
    }

    @Override
    public Void visit(ASTExplicitCall astExplicitCall) {
        return null;
    }

    @Override
    public Void visit(ASTMultDiv astMultDiv) {
        astMultDiv.left_expr.accept(this);
        astMultDiv.right_expr.accept(this);
        return null;
    }

    @Override
    public Void visit(ASTPlusMinus astPlusMinus) {
        astPlusMinus.left_expr.accept(this);
        astPlusMinus.right_expr.accept(this);
      //  left = (x + y - z);
      //  right = (a + b - c);
        // (x + y - z) + (a + b - c)
        //functie {
        // definit aici, sau ca membrii in clasa
        //      (x + y - z) + (a + b - c)
        //}
        // let l <- ((x + y - z) + (a + b - c))  : definit in functie, sau ca membrii in clasa
        //
        // Unde poti defini o variabila? Membru in clasa, let, case, in metode.
        // In contextul din care provin mizeriile, sa vedem ce tip au.

        // stiind ca x, y, z, a, b, c sunt INT
        // atunci rezulta INT
        // left este de tip INT
        // right e de tip INT

        // context al expresiei plus-minus <- ar trebui sa exista contextele de definire al subexpresiilor.
        //


        return null;
    }

    @Override
    public Void visit(ASTRelational astRelational) {
        astRelational.left_expr.accept(this);
        astRelational.right_expr.accept(this);

        return null;
    }

    @Override
    public Void visit(ASTAssign astAssign) {
        astAssign.id.accept(this);
        astAssign.expr.accept(this);

        return null;
    }

    @Override
    public Void visit(ASTImplicitCall astImplicitCall) {
        //astImplicitCall.
        return null;
    }

    @Override
    public Void visit(ASTFormal astFormal) {
        String id = astFormal.id.token.getText();
        String type = astFormal.type.getText();

        //System.out.println("DEFPASVISITOR FORMAL ID: " + id + " DEFPASSVISITOR type: " + type);

        IdSymbol sym = new IdSymbol(id);

        TypeSymbol class_scope = (TypeSymbol) currentScope.getParent();
        if (!currentScope.add(sym)) {
            SymbolTable.error(astFormal.context, astFormal.id.token, "Method " +
                    ((FunctionSymbol) currentScope).getName() + " of class " + class_scope.getName() + " redefines formal parameter " +
                    id);
            return null;
        }

        if (id.compareTo("self") == 0) {
            SymbolTable.error(astFormal.context, astFormal.id.token, "Method " +
                    ((FunctionSymbol) currentScope).getName() + " of class " + class_scope.getName() + " has formal parameter with illegal name self");
            return null;
        }

        if (type.compareTo("SELF_TYPE") == 0) {
            SymbolTable.error(astFormal.context, astFormal.type, "Method " +
                    ((FunctionSymbol) currentScope).getName() + " of class " + class_scope.getName() + " has formal parameter " +
                    astFormal.id.token.getText() + " with illegal type SELF_TYPE");
            return null;
        }

        sym.setType(astFormal.type.getText());
        astFormal.id.sym = sym;


        return null;
    }

    @Override
    public Void visit(ASTFormalInit astFormalInit) {
        String id = astFormalInit.id.token.getText();

        IdSymbol sym = new IdSymbol(id);

        if (!currentScope.add(sym)) {
            currentScope.modify(sym);
        }

        if (id.compareTo("self") == 0) {
            SymbolTable.error(astFormalInit.ctx, astFormalInit.id.token, "Let variable has illegal name self");
            return null;
        }

        astFormalInit.id.sym = sym;

        if (astFormalInit.expr != null) {
            astFormalInit.expr.accept(this);
        }

        return null;
    }

    @Override
    public Void visit(ASTClass astClass) {

        if (astClass.type.getText().compareTo("SELF_TYPE") == 0) {
            SymbolTable.error(astClass.context, astClass.type, "Class has illegal name SELF_TYPE");
        }

        if (astClass.parent_type != null) {
            String parent_type = astClass.parent_type.getText();

            if ((parent_type.compareTo("Int") == 0) ||
                    (parent_type.compareTo("Bool") == 0) ||
                    (parent_type.compareTo("String") == 0) || (parent_type.compareTo("SELF_TYPE") == 0)) {

                SymbolTable.error(astClass.context, astClass.parent_type, "Class " + astClass.type.getText() + " has illegal parent " + parent_type);
            }
        }

        TypeSymbol sym = new TypeSymbol(astClass.type.getText());
        if (!SymbolTable.globals.add(sym)) {
            SymbolTable.error(astClass.context, astClass.type, "Class " + astClass.type.getText() + " is redefined");
        }


        sym.setParent(currentScope);
        if (astClass.parent_type != null) {
            sym.setTypeParent(astClass.parent_type.getText());
        }

        currentScope = sym;
        for (ASTFeature f : astClass.features) {
            f.accept(this);
        }
        currentScope = sym.getParent();

        SymbolTable.globals.modify(sym);
        astClass.sym = sym;

        return null;
    }

    @Override
    public Void visit(ASTMethod astMethod) {
        FunctionSymbol sym = new FunctionSymbol(astMethod.id.token.getText(), currentScope);
        //System.out.println("DEFPASSVISITOR: " + astMethod.id.token.getText());
        if (!currentScope.add(sym)) {
                SymbolTable.error(astMethod.context, astMethod.id.token, "Class " + ((TypeSymbol)currentScope).getName() +
                        " redefines method " + astMethod.id.token.getText());
            return null;
        }

        TypeSymbol class_scope = (TypeSymbol) currentScope;
        currentScope = sym;
        for (ASTFormal f : astMethod.formals) {
          //  System.out.println("DEFPASSVISITOR formal: " + f.id.token.getText());
            f.accept(this);
        }
        astMethod.body.accept(this);
        currentScope = class_scope;
        sym.setReturnType(astMethod.type.getText());

        astMethod.id.sym = sym;
        currentScope.modify(sym);

        return null;
    }

    @Override
    public Void visit(ASTWhile astWhile) {
        astWhile.cond_expr.accept(this);
        astWhile.body.accept(this);
        return null;
    }

    @Override
    public Void visit(ASTBlock astBlock) {

        for (ASTExpression e : astBlock.exprs) {
            e.accept(this);
        }

        return null;
    }

    @Override
    public Void visit(ASTLet astLet) {
        FunctionSymbol let_scope = new FunctionSymbol("", currentScope);
        currentScope = let_scope;
        for (ASTFormalInit f : astLet.formals) {
            f.accept(this);
        }
        astLet.ex.accept(this);
        currentScope = currentScope.getParent();
        astLet.sym = let_scope;
        return null;
    }

    @Override
    public Void visit(ASTCase astCase) {

        astCase.expr.accept(this);

        for (ASTCaseBranch c : astCase.branches) {
            c.accept(this);
        }

        astCase.expr.accept(this);

        return null;
    }

    @Override
    public Void visit(ASTNew astNew) {

        return null;
    }

    @Override
    public Void visit(ASTIsVoid astIsVoid) {
        astIsVoid.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(ASTComplement astComplement) {
        astComplement.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(ASTNot astNot) {
        astNot.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(ASTSelf astSelf) {
        return null;
    }

    @Override
    public Void visit(ASTProg astProg) {
        currentScope = SymbolTable.globals;

        for (var cls: astProg.classes) {
            cls.accept(this);
        }

        return null;
    }

    @Override
    public Void visit(ASTIf astIf) {
        astIf.cond.accept(this);
        astIf.thenBranch.accept(this);
        astIf.elseBranch.accept(this);

        return null;
    }

    @Override
    public Void visit(ASTMember astMember) {
        Symbol sym = new IdSymbol(astMember.id.token.getText());
        if (!currentScope.add(sym)) {
            SymbolTable.error(astMember.context, astMember.id.token, "Class " + ((Symbol)currentScope).getName() + " redefines attribute " + astMember.id.token.getText());
            return null;
        }
        if (astMember.id.token.getText().compareTo("self") == 0) {
            SymbolTable.error(astMember.context, astMember.id.token, "Class " + ((Symbol)currentScope).getName() + " has attribute with illegal name self");
            return null;
        }
        astMember.id.sym = sym;

        if (astMember.expr != null) {
            astMember.expr.accept(this);
        }

        return null;
    }

    @Override
    public Void visit(ASTCaseBranch astCaseBranch) {

        if (astCaseBranch.id.token.getText().compareTo("self") == 0) {
            SymbolTable.error(astCaseBranch.ctx, astCaseBranch.id.token, "Case variable has illegal name self");
            return null;
        }

        FunctionSymbol case_scope = new FunctionSymbol("", currentScope);
        currentScope = case_scope;

        IdSymbol sym = new IdSymbol(astCaseBranch.id.token.getText());
        currentScope.add(sym);

        astCaseBranch.results.accept(this);

        currentScope = currentScope.getParent();

        astCaseBranch.id.sym = sym;

        astCaseBranch.sym = case_scope;
        return null;
    }

    @Override
    public Void visit(ASTType astType) {
        astType.accept(this);
        return null;
    }
};