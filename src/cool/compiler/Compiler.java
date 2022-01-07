package cool.compiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import cool.lexer.*;
import cool.parser.*;
import cool.structures.SymbolTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Compiler {
    // Annotates class nodes with the names of files where they are defined.
    public static ParseTreeProperty<String> fileNames = new ParseTreeProperty<>();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No file(s) given");
            return;
        }
        
        CoolLexer lexer = null;
        CommonTokenStream tokenStream = null;
        CoolParser parser = null;
        ParserRuleContext globalTree = null;
        
        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;
        
        // Parse each input file and build one big parse tree out of
        // individual parse trees.
        for (var fileName : args) {
            var input = CharStreams.fromFileName(fileName);
            
            // Lexer
            if (lexer == null)
                lexer = new CoolLexer(input);
            else
                lexer.setInputStream(input);

            // Token stream
            if (tokenStream == null)
                tokenStream = new CommonTokenStream(lexer);
            else
                tokenStream.setTokenSource(lexer);
                
            /*
            // Test lexer only.
            tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();
            tokens.stream().forEach(token -> {
                var text = token.getText();
                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());
                
                System.out.println(text + " : " + name);
                //System.out.println(token);
            });
            */
            
            // Parser
            if (parser == null)
                parser = new CoolParser(tokenStream);
            else
                parser.setTokenStream(tokenStream);

            // Customized error listener, for including file names in error
            // messages.
            var errorListener = new BaseErrorListener() {
                public boolean errors = false;
                
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    String newMsg = "\"" + new File(fileName).getName() + "\", line " +
                                        line + ":" + (charPositionInLine + 1) + ", ";
                    
                    Token token = (Token)offendingSymbol;
                    if (token.getType() == CoolLexer.ERROR)
                        newMsg += "Lexical error: " + token.getText();
                    else
                        newMsg += "Syntax error: " + msg;
                    
                    System.err.println(newMsg);
                    errors = true;
                }
            };
            
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            
            // Actual parsing
            var tree = parser.program();
            if (globalTree == null)
                globalTree = tree;
            else
                // Add the current parse tree's children to the global tree.
                for (int i = 0; i < tree.getChildCount(); i++)
                    globalTree.addAnyChild(tree.getChild(i));
                    
            // Annotate class nodes with file names, to be used later
            // in semantic error messages.
            for (int i = 0; i < tree.getChildCount(); i++) {
                var child = tree.getChild(i);
                // The only ParserRuleContext children of the program node
                // are class nodes.
                if (child instanceof ParserRuleContext)
                    fileNames.put(child, fileName);
            }
            
            // Record any lexical or syntax errors.
            lexicalSyntaxErrors |= errorListener.errors;
        }

        // Stop before semantic analysis phase, in case errors occurred.
        if (lexicalSyntaxErrors) {
            System.err.println("Compilation halted");
            return;
        }

        var astConstructionVisitor = new CoolParserBaseVisitor<ASTNode>() {
            @Override public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                List<ASTClass> cls = new ArrayList<>();
                for (var x : ctx.cls()) {
                    cls.add((ASTClass) visit(x));
                }
                return new ASTProg((ParserRuleContext) ctx, ctx.start, cls);
            }

            @Override public ASTNode visitCls(CoolParser.ClsContext ctx) {
                Token type = ctx.type;
                Token parent_type = ctx.parent;
                List<ASTFeature> features = new ArrayList<>();
                for (var x : ctx.feature()) {
                    features.add((ASTFeature) visit(x));
                }
                return new ASTClass((ParserRuleContext) ctx, ctx.start, type, parent_type, features);
            }

            @Override public ASTNode visitMethod(CoolParser.MethodContext ctx) {
                List<ASTFormal> formals = new ArrayList<>();
                for (int i = 0; i < ctx.formal().size(); i++) {
                    formals.add((ASTFormal) visit(ctx.formal(i)));
                }
                ASTExpression expr = (ASTExpression) visit(ctx.ex);
                ASTId id = new ASTId(ctx, ctx.id);
                Token type = ctx.TYPE_ID().getSymbol();
                return new ASTMethod( ctx, id, formals, type, expr, ctx.start);
            }

            @Override public ASTNode visitMember(CoolParser.MemberContext ctx) {

                ASTExpression expr = null;
                Token assign = null;
                if (ctx.ex != null) {
                    expr = (ASTExpression) visit(ctx.ex);
                    assign = ctx.ASSIGN().getSymbol();
                }
                ASTFormal formal = (ASTFormal)visit(ctx.formal());


                return new ASTMember((ParserRuleContext) ctx, new ASTId((ParserRuleContext) ctx, ctx.formal().id), formal.type, assign, expr, ctx.start);
            }

            @Override public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                Token type = null;
                type = ctx.type;
                ASTId id = new ASTId((ParserRuleContext) ctx, ctx.id);
                return new ASTFormal((ParserRuleContext) ctx, id, type, ctx.start);

            }

            @Override public ASTNode visitFormal_init(CoolParser.Formal_initContext ctx) {
                ASTExpression expr = null;
                Token assign = null;
                if (ctx.ex != null) {
                    expr = (ASTExpression) visit(ctx.ex);
                    assign = ctx.ASSIGN().getSymbol();
                }
                ASTFormal formal = (ASTFormal)visit(ctx.formal());


                return new ASTFormalInit((ParserRuleContext) ctx, new ASTId((ParserRuleContext) ctx, ctx.formal().id), formal.type, assign, expr, ctx.start);
            }

            @Override public ASTNode visitNew(CoolParser.NewContext ctx) { return new ASTNew((ParserRuleContext) ctx,ctx.NEW().getSymbol(), ctx.TYPE_ID().getSymbol(), ctx.start); }

            @Override public ASTNode visitPlusMinus(CoolParser.PlusMinusContext ctx) {
                return new ASTPlusMinus((ParserRuleContext) ctx,(ASTExpression)visit(ctx.left), (ASTExpression)visit(ctx.right), ctx.op, ctx.start);
            }

            @Override public ASTNode visitBool(CoolParser.BoolContext ctx) { return new ASTBool((ParserRuleContext) ctx,ctx.BOOL().getSymbol()); }

            @Override public ASTNode visitString(CoolParser.StringContext ctx) { return new ASTString((ParserRuleContext) ctx,ctx.STRING().getSymbol()); }

            @Override public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
                return new ASTIsVoid((ParserRuleContext) ctx,ctx.ISVOID().getSymbol(), (ASTExpression)visit(ctx.ex), ctx.start);
            }

            @Override public ASTNode visitWhile(CoolParser.WhileContext ctx) {
                return new ASTWhile((ParserRuleContext) ctx,(ASTExpression) visit(ctx.cond), (ASTExpression) visit(ctx.body), ctx.start, ctx.WHILE().getSymbol());
            }

            @Override public ASTNode visitInt(CoolParser.IntContext ctx) { return new ASTInt((ParserRuleContext) ctx,ctx.INT().getSymbol()); }

            @Override public ASTNode visitCall_explicit(CoolParser.Call_explicitContext ctx) {
                Token type = null;
                if (ctx.TYPE_ID() != null) {
                    type = ctx.TYPE_ID().getSymbol();
                }
                ASTExpression expr = (ASTExpression) visit (ctx.ex);
                ASTId id = new ASTId((ParserRuleContext) ctx, ctx.ID().getSymbol());
                List<ASTExpression> ex = new ArrayList<>();
                for (int i = 1; i < ctx.expr().size() ; i++) {
                    ex.add((ASTExpression)visit(ctx.expr().get(i)));
                }
                return new ASTExplicitCall((ParserRuleContext) ctx, expr, type, id, ex, ctx.start);
            }

            @Override public ASTNode visitNot(CoolParser.NotContext ctx) {
                return new ASTNot((ParserRuleContext) ctx, ctx.NOT().getSymbol(), (ASTExpression)visit(ctx.ex), ctx.start);
            }

            @Override public ASTNode visitParen(CoolParser.ParenContext ctx) { return visit(ctx.ex); }

            @Override public ASTNode visitMultDiv(CoolParser.MultDivContext ctx) {
                return new ASTMultDiv((ParserRuleContext) ctx, (ASTExpression)visit(ctx.left), (ASTExpression)visit(ctx.right), ctx.op, ctx.start);
            }

            @Override public ASTNode visitSelf(CoolParser.SelfContext ctx) { return new ASTSelf((ParserRuleContext) ctx, ctx.start); }

            @Override public ASTNode visitCall_implicit(CoolParser.Call_implicitContext ctx) {
                List<ASTExpression> expr = new ArrayList<>();
                for (int i = 0; i < ctx.expr().size() ; i++) {
                    expr.add((ASTExpression)visit(ctx.expr().get(i)));
                }
                return new ASTImplicitCall((ParserRuleContext) ctx, new ASTId((ParserRuleContext) ctx, ctx.ID().getSymbol()), expr, ctx.start);
            }

            @Override public ASTNode visitBlock(CoolParser.BlockContext ctx) {
                List<ASTExpression> expr = new ArrayList<>();
                for (int i = 0; i < ctx.expr().size() ; i++) {
                    expr.add((ASTExpression)visit(ctx.expr().get(i)));
                }
                return new ASTBlock((ParserRuleContext) ctx, ctx.start, expr);
            }

            @Override public ASTNode visitLet(CoolParser.LetContext ctx) {
                List<ASTFormalInit> formals = new ArrayList<>();
                ASTExpression body = (ASTExpression) visit(ctx.body);

                for (var x : ctx.formal_init()) {
                    formals.add((ASTFormalInit) visit(x));
                }
                return new ASTLet((ParserRuleContext) ctx,ctx.start, formals, body, ctx.LET().getSymbol());

            }

            @Override public ASTNode visitRelational(CoolParser.RelationalContext ctx) {
                return new ASTRelational((ParserRuleContext) ctx, (ASTExpression)visit(ctx.left), (ASTExpression)visit(ctx.right), ctx.op, ctx.start);
            }

            @Override public ASTNode visitId(CoolParser.IdContext ctx) { return new ASTId((ParserRuleContext) ctx,ctx.start); }

            @Override public ASTNode visitComplement(CoolParser.ComplementContext ctx) {
                return new ASTComplement((ParserRuleContext) ctx,ctx.COMPLEMENT().getSymbol(), (ASTExpression)visit(ctx.ex), ctx.start);
            }

            @Override public ASTNode visitIf(CoolParser.IfContext ctx) {
                return new ASTIf((ParserRuleContext) ctx, (ASTExpression)visit(ctx.cond), (ASTExpression)visit(ctx.thenBranch), (ASTExpression)visit(ctx.elseBranch), ctx.start);
            }

            @Override public ASTNode visitCase_branch(CoolParser.Case_branchContext ctx) {
                ASTId id = new ASTId((ParserRuleContext) ctx,ctx.id);
                Token type = ctx.type;
                ASTExpression expr = (ASTExpression) visit(ctx.result);
                return new ASTCaseBranch((ParserRuleContext) ctx, ctx.start, id, type, expr);
            }
            @Override public ASTNode visitCase(CoolParser.CaseContext ctx) {
                List<ASTCaseBranch> branches = new ArrayList<>();
                for (var x : ctx.case_branch()) {
                    ASTCaseBranch branch = (ASTCaseBranch) visit(x);
                    branches.add(branch);
                }
                return new ASTCase((ParserRuleContext) ctx, ctx.start, branches, (ASTExpression) visit(ctx.ex), ctx.CASE().getSymbol());
            }

            @Override public ASTNode visitAssign(CoolParser.AssignContext ctx) { return new ASTAssign((ParserRuleContext) ctx,new ASTId((ParserRuleContext) ctx, ctx.id),  ctx.ASSIGN().getSymbol(), (ASTExpression) visit(ctx.ex), ctx.start); }


        };

        var ast = astConstructionVisitor.visit(globalTree);        // Populate global scope.



        var printVisitor = new ASTVisitor<Void>() {
            int indent = 0;

            @Override
            public Void visit(ASTId astId) {
                printIndent(astId.token.getText());
                return null;
            }

            @Override
            public Void visit(ASTInt astInt) {
                printIndent(astInt.token.getText());
                return null;
            }

            @Override
            public Void visit(ASTBool astBool) {
                printIndent(astBool.token.getText());
                return null;
            }

            @Override
            public Void visit(ASTString astString) {
                printIndent(astString.token.getText());
                return null;
            }

            @Override
            public Void visit(ASTExplicitCall astExplicitCall) {
                printIndent(".");
                indent += 2;
                astExplicitCall.expr.accept(this);
                if (astExplicitCall.type != null) printIndent(astExplicitCall.type.getText());
                astExplicitCall.id.accept(this);
                for (var a : astExplicitCall.args) {
                    a.accept(this);
                }
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTMultDiv astMultDiv) {
                printIndent(astMultDiv.type.getText());

                indent += 2;
                astMultDiv.left_expr.accept(this);
                astMultDiv.right_expr.accept(this);
                indent -= 2;
                return null;

            }

            @Override
            public Void visit(ASTPlusMinus astPlusMinus) {
                printIndent(astPlusMinus.op.getText());

                indent += 2;
                astPlusMinus.left_expr.accept(this);
                astPlusMinus.right_expr.accept(this);
                indent -= 2;
                return null;

            }

            @Override
            public Void visit(ASTRelational astRelational) {
                printIndent(astRelational.op.getText());

                indent += 2;
                astRelational.left_expr.accept(this);
                astRelational.right_expr.accept(this);
                indent -= 2;
                return null;

            }

            @Override
            public Void visit(ASTAssign astAssign) {

                printIndent(astAssign.assign.getText());
                indent += 2;
                astAssign.id.accept(this);
                astAssign.expr.accept(this);
                indent -= 2;
                return null;

            }

            @Override
            public Void visit(ASTImplicitCall astImplicitCall) {
                printIndent("implicit dispatch");
                indent += 2;
                astImplicitCall.id.accept(this);
                for (var a : astImplicitCall.args) {
                    a.accept(this);
                }
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTFormal astFormal) {
                printIndent("formal");
                indent += 2;
                astFormal.id.accept(this);
                printIndent(astFormal.type.getText());
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTFormalInit astFormalInit) {
                printIndent("local");
                indent += 2;
                astFormalInit.id.accept(this);
                printIndent(astFormalInit.type.getText());
                if (astFormalInit.expr != null)  astFormalInit.expr.accept(this);
                indent -= 2;

                return null;
            }

            @Override
            public Void visit(ASTClass astClass) {
                printIndent("class");
                indent+=2;
                printIndent(astClass.type.getText());
                if (astClass.parent_type != null) printIndent(astClass.parent_type.getText());

                for (var f : astClass.features) {
                    f.accept(this);
                }

                indent-=2;
                return null;
            }

            @Override
            public Void visit(ASTMethod astMethod) {
                printIndent("method");
                indent += 2;
                astMethod.id.accept(this);
                for (var f : astMethod.formals) {
                    f.accept(this);
                }
                printIndent(astMethod.type.getText());
                astMethod.body.accept(this);
                indent -= 2;
                return null;

            }

            @Override
            public Void visit(ASTWhile astWhile) {
                printIndent(astWhile.WHILE.getText());

                indent += 2;
                astWhile.cond_expr.accept(this);
                astWhile.body.accept(this);
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTBlock astBlock) {
                printIndent("block");
                indent += 2;
                for (var x : astBlock.exprs) {
                    x.accept(this);
                }
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTLet astLet) {
                printIndent(astLet.let.getText());
                indent += 2;
                for (var f : astLet.formals) {
                    f.accept(this);
                }
                astLet.ex.accept(this);
                indent -= 2;

                return null;
            }

            @Override
            public Void visit(ASTCase astCase) {
                printIndent(astCase.CASE.getText());
                indent += 2;
                astCase.expr.accept(this);
                for (var x : astCase.branches) {
                    x.accept(this);
                }
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTNew astNew) {
                printIndent(astNew.NEW.getText());
                indent += 2;
                printIndent(astNew.type.getText());
                indent -= 2;
                return null;

            }

            @Override
            public Void visit(ASTIsVoid astIsVoid) {
                printIndent(astIsVoid.isVoid.getText());
                indent += 2;
                astIsVoid.expr.accept(this);
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTComplement astComplement) {
                printIndent(astComplement.complement.getText());

                indent += 2;
                astComplement.expr.accept(this);
                indent -= 2;

                return null;

            }

            @Override
            public Void visit(ASTNot astNot) {

                printIndent(astNot.not.getText());

                indent += 2;
                astNot.expr.accept(this);
                indent -= 2;

                return null;

            }

            @Override
            public Void visit(ASTSelf astSelf) {
                printIndent(astSelf.token.getText());
                return null;
            }

            @Override
            public Void visit(ASTProg astProg) {
                printIndent("program");
                indent += 2;
                for (var cls : astProg.classes) {
                    cls.accept(this);
                }
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTIf astIf) {
                printIndent("if");
                indent += 2;
                astIf.cond.accept(this);
                astIf.thenBranch.accept(this);
                astIf.elseBranch.accept(this);
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTMember astMember) {
                printIndent("attribute");
                indent+=2;
                astMember.id.accept(this);
                printIndent(astMember.type.getText());
                if (astMember.expr != null) {
                    astMember.expr.accept(this);
                }
                indent-=2;
                return null;
            }

            @Override
            public Void visit(ASTCaseBranch astCaseBranch) {
                printIndent("case branch");
                indent += 2;
                astCaseBranch.id.accept(this);
                printIndent(astCaseBranch.type.getText());
                astCaseBranch.results.accept(this);
                indent -= 2;
                return null;
            }

            @Override
            public Void visit(ASTType astType) {
                return null;
            }

            void printIndent(String str) {
                for (int i = 0; i < indent; i++)
                    System.out.print(" ");
                System.out.println(str);
            }


        };

       // ast.accept(printVisitor);

        SymbolTable.defineBasicClasses();

        DefinitionPassVisitor defVisitor = new DefinitionPassVisitor();
        ast.accept(defVisitor);

        ResolutionPassVisitor resVisitor = new ResolutionPassVisitor();
        ast.accept(resVisitor);
        
        // TODO Semantic analysis
        
        if (SymbolTable.hasSemanticErrors()) {
            System.err.println("Compilation halted");
            return;
        }
    }
}
