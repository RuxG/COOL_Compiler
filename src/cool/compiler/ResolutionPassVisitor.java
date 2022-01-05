package cool.compiler;

import cool.compiler.*;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

import java.lang.reflect.Type;

public class ResolutionPassVisitor implements ASTVisitor<Void> {
    Scope current_scope = null;


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
    // String evaluate_type(ASTExpression) {
    //
    // }
    @Override
    public Void visit(ASTPlusMinus astPlusMinus) {
        // left insanceof ASTString
        // left si right sunt ASTInt, ASTId, ASTPlusMinus, ASTMultDiv


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
        current_scope = SymbolTable.globals;

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