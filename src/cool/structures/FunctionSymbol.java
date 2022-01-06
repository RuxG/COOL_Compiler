package cool.structures;

import java.util.LinkedHashMap;
import java.util.Map;

// O functie este atât simbol, cât și domeniu de vizibilitate pentru parametrii
// săi formali.

/*
 TODO 1: Implementați clasa FunctionSymbol, suprascriind metodele din interfață
        și adăugându-i un nume.
 */
public class FunctionSymbol extends IdSymbol implements Scope {
    protected Scope parent;

    // LinkedHashMap reține ordinea adăugării.
    protected Map<String, Symbol> symbols = new LinkedHashMap<>();

    protected String returnType;

    public FunctionSymbol(String name, Scope parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol s) {
        // Ne asigurăm că simbolul nu există deja în domeniul de vizibilitate
        // curent.
        if (symbols.containsKey(s.getName()))
            return false;

        symbols.put(s.getName(), s);

        return true;
    }

    @Override
    public boolean modify(Symbol sym) {
        symbols.put(sym.name, sym);
        return true;
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
}