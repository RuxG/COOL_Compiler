package cool.structures;

public class Symbol {
    protected String name;
    protected TypeSymbol type;
    
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

}
