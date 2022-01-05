package cool.structures;

public interface Scope {
    public boolean add(Symbol sym);

    public boolean modify(Symbol sym);

    public Symbol lookup(String str, String symbolCategory);
    
    public Scope getParent();
}
