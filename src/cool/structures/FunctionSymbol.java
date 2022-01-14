package cool.structures;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends IdSymbol implements Scope {
    protected Scope parent;

    protected Map<String, Symbol> symbols = new LinkedHashMap<>();

    protected String returnType;

    public FunctionSymbol(String name, Scope parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol s) {

        if (symbols.containsKey(s.getName()))
            return false;

        symbols.put(s.getName(), s);

        return true;
    }

    @Override
    public void modify(Symbol sym) {
        symbols.put(sym.name, sym);
    }

    @Override
    public Symbol lookup(String str, String symbolCategory) {
        Symbol sym = symbols.get(str);

        if (sym != null) {
            return sym;
        }

        if (parent != null) {
            return parent.lookup(str, symbolCategory);
        }

        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Map<String, Symbol> getSymbolTable() {
        return symbols;
    }

    public void setSymbolTable(Map<String, Symbol> symbol_table) {
        this.symbols = symbol_table;
    }
}