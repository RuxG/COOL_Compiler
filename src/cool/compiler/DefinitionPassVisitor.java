package cool.compiler;

import cool.compiler.*;
import cool.structures.Scope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

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
        return null;
    }

    @Override
    public Void visit(ASTPlusMinus astPlusMinus) {
        ASTExpression left;
        ASTExpression right;
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
        return null;
    }

    @Override
    public Void visit(ASTAssign astAssign) {
        return null;
    }

    @Override
    public Void visit(ASTImplicitCall astImplicitCall) {
        return null;
    }

    @Override
    public Void visit(ASTFormal astFormal) {

        return null;
    }

    @Override
    public Void visit(ASTFormalInit astFormalInit) {
        return null;
    }

    @Override
    public Void visit(ASTClass astClass) {

        if (astClass.type.getText().compareTo("SELF_TYPE") == 0) {
            SymbolTable.error(astClass.context, astClass.start, "Class has illegal name SELF_TYPE");
        }

        if (astClass.parent_type != null) {
            String parent_type = astClass.parent_type.getText();

            if ((parent_type.compareTo("Object") == 0) || (parent_type.compareTo("Int") == 0) ||
                    (parent_type.compareTo("Bool") == 0) ||
                    (parent_type.compareTo("String") == 0) || (parent_type.compareTo("SELF_TYPE") == 0)) {

                SymbolTable.error(astClass.context, astClass.start, "Class " + astClass.type.getText() + " has illegal parent " + parent_type);
            }
        }

        TypeSymbol sym = new TypeSymbol(astClass.type.getText());
        if (!SymbolTable.globals.add(sym)) {
            SymbolTable.error(astClass.context, astClass.start, "Class " + astClass.type.getText() + " is redefined");
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

        return null;
    }

    @Override
    public Void visit(ASTMethod astMethod) {
        return null;
    }

    @Override
    public Void visit(ASTWhile astWhile) {
        return null;
    }

    @Override
    public Void visit(ASTBlock astBlock) {
        return null;
    }

    @Override
    public Void visit(ASTLet astLet) {
        return null;
    }

    @Override
    public Void visit(ASTCase astCase) {
        return null;
    }

    @Override
    public Void visit(ASTNew astNew) {
        return null;
    }

    @Override
    public Void visit(ASTIsVoid astIsVoid) {
        return null;
    }

    @Override
    public Void visit(ASTComplement astComplement) {
        return null;
    }

    @Override
    public Void visit(ASTNot astNot) {
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
        return null;
    }

    @Override
    public Void visit(ASTMember astMember) {
        return null;
    }

    @Override
    public Void visit(ASTCaseBranch astCaseBranch) {
        return null;
    }

    @Override
    public Void visit(ASTType astType) {
        return null;
    }
};