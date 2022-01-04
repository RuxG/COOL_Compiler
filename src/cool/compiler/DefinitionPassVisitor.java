package cool.compiler;

import cool.compiler.*;
import cool.structures.Scope;

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
};