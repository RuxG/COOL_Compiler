package cool.structures;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class TypeSymbol extends Symbol implements Scope {

    public static TypeSymbol OBJECT = new TypeSymbol("Object");
    public static TypeSymbol INT = new TypeSymbol("Int");
    public static TypeSymbol BOOL = new TypeSymbol("Bool");
    public static TypeSymbol STRING = new TypeSymbol("String");
    public static TypeSymbol IO = new TypeSymbol("IO");

    protected Map<String, Symbol> membersSymbols = new LinkedHashMap<>();
    protected Map<String, Symbol> methodsSymbols = new LinkedHashMap<>();

    Scope parent = null;
    String typeParent = null;

    public TypeSymbol(String name) {
        super(name);
    }

    @Override
    public boolean add(Symbol sym) {
        if (sym instanceof FunctionSymbol) {
            if (methodsSymbols.containsKey(sym.name)) {
                return false;
            }
            methodsSymbols.put(sym.name, sym);
        } else {
            if (membersSymbols.containsKey(sym.name)) {
                return false;
            }
            membersSymbols.put(sym.name, sym);
        }
        return true;
    }

    @Override
    public boolean modify(Symbol sym) {
        return false;
    }

    @Override
    public Symbol lookup(String str, String symbolCategory) {
        Symbol sym = null;
        if (symbolCategory.compareTo("methodSymbol") == 0) {
            sym = methodsSymbols.get(str);
        } else if (symbolCategory.compareTo("memberSymbol") == 0) {
            sym = membersSymbols.get(str);
        } else {
            sym = methodsSymbols.get(str);
            if (sym == null) {
                sym = membersSymbols.get(str);
            }
        }

        if (sym != null) {
            return sym;
        }

        if (typeParent != null) {
            TypeSymbol parent_type_sym = (TypeSymbol) parent.lookup(typeParent, "");
            return parent_type_sym.lookup(str, "methodSymbol");
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

    public String getTypeParent() {
        return typeParent;
    }

    public void setTypeParent(String typeParent) {
        this.typeParent = typeParent;
    }


}
