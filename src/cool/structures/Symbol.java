package cool.structures;

public class Symbol {
    protected String name;
    protected String type;

    public Symbol(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getName();
    }

}
