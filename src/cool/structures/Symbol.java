package cool.structures;

import org.antlr.v4.runtime.ParserRuleContext;

public class Symbol {
    protected String name;
    protected TypeSymbol type;
    protected String type_str;
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
    public void setTypeStr(String type) {
        this.type_str = type;
    }
    public String getTypeStr() {
        return type_str;
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
