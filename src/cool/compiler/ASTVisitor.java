package cool.compiler;

public interface ASTVisitor<T>{
    T visit(ASTId astId);

    T visit(ASTInt astInt);

    T visit(ASTBool astBool);

    T visit(ASTString astString);

    T visit(ASTExplicitCall astExplicitCall);

    T visit(ASTMultDiv astMultDiv);

    T visit(ASTPlusMinus astPlusMinus);

    T visit(ASTRelational astRelational);

    T visit(ASTAssign astAssign);

    T visit(ASTImplicitCall astImplicitCall);

    T visit(ASTFormal astFormal);

    T visit(ASTFormalInit astFormalInit);

    T visit(ASTClass astClass);

    T visit(ASTMethod astMethod);


    T visit(ASTWhile astWhile);

    T visit(ASTBlock astBlock);

    T visit(ASTLet astLet);

    T visit(ASTCase astCase);

    T visit(ASTNew astNew);

    T visit(ASTIsVoid astIsVoid);

    T visit(ASTComplement astComplement);

    T visit(ASTNot astNot);

    T visit(ASTSelf astSelf);

    T visit(ASTProg astProg);

    T visit(ASTIf astIf);

    T visit(ASTMember astMember);

    T visit(ASTCaseBranch astCaseBranch);
}
