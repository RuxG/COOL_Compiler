package cool.structures;

import java.util.LinkedHashMap;
import java.util.Map;

public class CaseSymbol extends Symbol implements Scope {

    protected Map<String, Symbol> symbols = new LinkedHashMap<>();

    protected Scope parent;

    public CaseSymbol(String name, Scope parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol sym) {
        // Ne asigurăm că simbolul nu există deja în domeniul de vizibilitate
        // curent.
        if (symbols.containsKey(sym.getName()))
            return false;

        symbols.put(sym.getName(), sym);

        return true;
    }

    @Override
    public boolean modify(Symbol sym) {
        return false;
    }

    @Override
    public Symbol lookup(String s, String symbolCategory) {
        var sym = symbols.get(s);

        if (sym != null)
            return sym;

        // Dacă nu găsim simbolul în domeniul de vizibilitate curent, îl căutăm
        // în domeniul de deasupra.
        if (parent != null)
            return parent.lookup(s, symbolCategory);

        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
}
