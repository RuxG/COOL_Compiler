package cool.structures;

import org.antlr.v4.runtime.ParserRuleContext;

public class Symbol {
    protected String name;
    protected TypeSymbol type;
    protected ParserRuleContext ctx;

    public Symbol(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setType(TypeSymbol type) {
        this.type = type;
    }

    public TypeSymbol getType() {
        return type;
    }
    @Override
    public String toString() {
        return getName();
    }

    public void setContext(ParserRuleContext ctx) {
        this.ctx = ctx;
    }

    public ParserRuleContext getContext() {
        return ctx;
    }

}
