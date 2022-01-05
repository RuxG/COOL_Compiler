package cool.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import javax.naming.Context;
import java.util.List;

// Rădăcina ierarhiei de clase reprezentând nodurile arborelui de sintaxă
// abstractă (AST). Singura metodă permite primirea unui visitor.
public abstract class ASTNode {
    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

// Orice expresie.
abstract class ASTExpression extends ASTNode {
    // Reținem un token descriptiv al expresiei, pentru a putea afișa ulterior
    // informații legate de linia și coloana eventualelor erori semantice.
    Token token;
    ParserRuleContext ctx;

    ASTExpression(ParserRuleContext ctx, Token token) {
        this.ctx = ctx; this.token = token;
    }
}

// Identificatori
class ASTId extends ASTExpression {
    ASTId(ParserRuleContext context, Token token) {
        super(context, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Literali întregi
class ASTInt extends ASTExpression {
    ASTInt(ParserRuleContext context, Token token) {
        super(context, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Booleeni
class ASTBool extends ASTExpression {
    ASTBool(ParserRuleContext context, Token token) {
        super(context, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// String
class ASTString extends ASTExpression {
    ASTString(ParserRuleContext context, Token token) { super(context, token); }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTAssign extends ASTExpression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.
    ASTId id;
    Token assign;
    ASTExpression expr;

    ASTAssign(ParserRuleContext context, ASTId id,
              Token assign, ASTExpression expr,
              Token start) {
        super(context, start);
        this.id = id;
        this.assign = assign;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTRelational extends ASTExpression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.

    ASTExpression left_expr;
    ASTExpression right_expr;
    Token op;

    ASTRelational(ParserRuleContext context, ASTExpression left_expr,
                  ASTExpression right_expr, Token op,
                  Token start) {
        super(context, start);
        this.left_expr = left_expr;
        this.right_expr = right_expr;
        this.op = op;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTPlusMinus extends ASTExpression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.

    ASTExpression left_expr;
    ASTExpression right_expr;
    Token op;

    ASTPlusMinus(ParserRuleContext context, ASTExpression left_expr,
                 ASTExpression right_expr, Token op,
                 Token start) {
        super(context, start);
        this.left_expr = left_expr;
        this.right_expr = right_expr;
        this.op = op;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTMultDiv extends ASTExpression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.

    ASTExpression left_expr;
    ASTExpression right_expr;
    Token type;

    ASTMultDiv(ParserRuleContext context, ASTExpression left_expr,
               ASTExpression right_expr, Token type,
               Token start) {
        super(context, start);
        this.left_expr = left_expr;
        this.right_expr = right_expr;
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTExplicitCall extends ASTExpression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.
    ASTExpression expr;
    Token type;
    ASTId id;
    List<ASTExpression> args;

    ASTExplicitCall(ParserRuleContext context, ASTExpression expr, Token type, ASTId id,
            List<ASTExpression> args,
            Token start) {
        super(context, start);
        this.expr = expr;
        this.type = type;
        this.id = id;
        this.args = args;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTImplicitCall extends ASTExpression {
    ASTId id;
    List<ASTExpression> args;

    ASTImplicitCall(ParserRuleContext context, ASTId id,
            List<ASTExpression> args,
            Token start) {
        super(context, start);
        this.id = id;
        this.args = args;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTMember extends ASTFeature {
    Token assign;
    ASTExpression expr;
    ASTId id;
    Token type;

    ASTMember(ParserRuleContext context, ASTId id, Token type, Token assign, ASTExpression expr, Token start) {
        super(context, start);
        this.id = id;
        this.type =  type;
        this.assign = assign;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTFormalInit extends ASTExpression {
    Token assign;
    ASTExpression expr;
    ASTId id;
    Token type;

    ASTFormalInit(ParserRuleContext context, ASTId id, Token type, Token assign, ASTExpression expr, Token start) {
        super(context, start);
        this.id = id;
        this.type =  type;
        this.assign = assign;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTCaseBranch extends ASTExpression {
    ASTId id;
    Token type;
    ASTExpression results;

    ASTCaseBranch(ParserRuleContext context, Token start, ASTId id, Token type,  ASTExpression results) {
        super(context, start);
        this.id = id;
        this.type = type;
        this.results = results;
    }
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}

class ASTWhile extends ASTExpression {
    ASTExpression cond_expr;
    ASTExpression body;
    Token WHILE;

    ASTWhile( ParserRuleContext context, ASTExpression cond_expr, ASTExpression body, Token token, Token WHILE) {
        super(context, token);
        this.cond_expr = cond_expr;
        this.body = body;
        this.WHILE = WHILE;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTBlock extends ASTExpression {
    List<ASTExpression> exprs;

    ASTBlock(ParserRuleContext context, Token token, List<ASTExpression> exprs) {
        super(context, token);
        this.exprs = exprs;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTLet extends ASTExpression {
    List<ASTFormalInit> formals;
    ASTExpression ex;
    Token let;

    ASTLet(ParserRuleContext context, Token token, List<ASTFormalInit> formals, ASTExpression expr, Token let) {
        super(context, token);
        this.formals = formals;
        this.let  = let;
        this.ex = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTCase extends ASTExpression {
    ASTExpression expr;
    List<ASTCaseBranch> branches;
    Token CASE;

    ASTCase(ParserRuleContext context, Token token, List<ASTCaseBranch> branches, ASTExpression expr, Token CASE) {
        super(context, token);
        this.branches = branches;
        this.expr = expr;
        this.CASE = CASE;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTNew extends ASTExpression {
    Token NEW;
    Token type;

    ASTNew(ParserRuleContext context, Token NEW, Token type, Token token) {
        super(context, token);
        this.NEW = NEW;
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTIsVoid extends ASTExpression {
    ASTExpression expr;
    Token isVoid;

    ASTIsVoid( ParserRuleContext context, Token isVoid, ASTExpression expr, Token token) {
        super(context, token);
        this.expr = expr;
        this.isVoid = isVoid;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTComplement extends ASTExpression {
    ASTExpression expr;
    Token complement;

    ASTComplement(ParserRuleContext context, Token complement, ASTExpression expr, Token token) {
        super(context, token);
        this.expr = expr;
        this.complement = complement;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTNot extends ASTExpression {
    ASTExpression expr;
    Token not;

    ASTNot(ParserRuleContext context, Token not,  ASTExpression expr, Token token) {
        super(context, token);
        this.expr = expr;
        this.not = not;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTSelf extends ASTExpression {

    ASTSelf(ParserRuleContext context, Token token) {
        super(context, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}


class ASTFormal extends ASTNode {
    ParserRuleContext context;
    Token start;
    ASTId id;
    Token type;

    ASTFormal(ParserRuleContext context, ASTId id, Token type, Token start) {
        this.context = context;
        this.start = start;
        this.type = type;
        this.id = id;
    }
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class ASTFeature extends ASTNode {
    Token start;
    ParserRuleContext context;
    ASTFeature(ParserRuleContext context, Token start) {
        this.context = context; this.start = start;
    }
}

class ASTMethod extends ASTFeature {
    ASTId id;
    ParserRuleContext context;
    List<ASTFormal> formals;
    Token type;
    ASTExpression body;

    ASTMethod(ParserRuleContext context,  ASTId id, List<ASTFormal> formals, Token type, ASTExpression body, Token start) {
        super(context, start);
        this.id = id;
        this.formals = formals;
        this.type = type;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}


class ASTClass extends ASTNode {
    Token start;
    ParserRuleContext context;
    Token type;
    Token parent_type;
    List<ASTFeature> features;

    ASTClass(ParserRuleContext context, Token start, Token type, Token parent_type, List<ASTFeature> features) {
        this.start = start;
        this.context = context;
        this.type = type;
        this.parent_type = parent_type;
        this.features = features;
    }
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Program
class ASTProg extends ASTNode {
    Token token;
    ParserRuleContext context;
    List<ASTClass> classes;

    ASTProg(ParserRuleContext context, Token token, List<ASTClass> classes) {
        this.token = token;
        this.context = context;
        this.classes = classes;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Construcția if.
class ASTIf extends ASTExpression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.
    ASTExpression cond;
    ASTExpression thenBranch;
    ASTExpression elseBranch;

    ASTIf(ParserRuleContext context, ASTExpression cond,
          ASTExpression thenBranch,
          ASTExpression elseBranch,
          Token start) {
        super(context, start);
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTType extends ASTNode {
    Token start;

    ASTType(Token token) {
        start = token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
