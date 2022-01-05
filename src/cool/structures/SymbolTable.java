package cool.structures;

import java.io.File;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

import static cool.structures.TypeSymbol.OBJECT;

public class SymbolTable {
    public static Scope globals;
    
    private static boolean semanticErrors;
    
    public static void defineBasicClasses() {
        globals = new DefaultScope(null);
        semanticErrors = false;

        // TODO Populate global scope.

        {
            TypeSymbol.OBJECT.setParent(globals);

            FunctionSymbol abortSymbol = new FunctionSymbol("abort", TypeSymbol.OBJECT);
            abortSymbol.setType(TypeSymbol.OBJECT);
            TypeSymbol.OBJECT.add(abortSymbol);

            FunctionSymbol type_nameSymbol = new FunctionSymbol("type_name", TypeSymbol.OBJECT);
            type_nameSymbol.setType(TypeSymbol.STRING);
            TypeSymbol.OBJECT.add(type_nameSymbol);

            FunctionSymbol copySymbol = new FunctionSymbol("copy", TypeSymbol.OBJECT);
            // copySymbol.add(TypeSymbol.CE_TIP?????)  -- SELF_TYPE
            TypeSymbol.OBJECT.add(copySymbol);
        }

        {
            TypeSymbol.INT.setParent(globals);
            TypeSymbol.INT.setTypeParent(TypeSymbol.OBJECT.name);
        }

        {
            TypeSymbol.BOOL.setParent(globals);
            TypeSymbol.BOOL.setTypeParent(TypeSymbol.OBJECT.name);
        }

        {
            TypeSymbol.STRING.setParent(globals);
            TypeSymbol.STRING.setTypeParent(TypeSymbol.OBJECT.name);

            FunctionSymbol lengthSymbol = new FunctionSymbol("length", TypeSymbol.STRING);
            lengthSymbol.setType(TypeSymbol.INT);
            TypeSymbol.STRING.add(lengthSymbol);

            FunctionSymbol concatSymbol = new FunctionSymbol("concat", TypeSymbol.STRING);
            concatSymbol.setType(TypeSymbol.STRING);
            TypeSymbol.STRING.add(concatSymbol);

            FunctionSymbol substrSymbol = new FunctionSymbol("substr", TypeSymbol.STRING);
            substrSymbol.setType(TypeSymbol.STRING);
            TypeSymbol.STRING.add(substrSymbol);
        }

        {
            TypeSymbol.IO.setParent(globals);
            TypeSymbol.IO.setTypeParent(TypeSymbol.OBJECT.name);

            FunctionSymbol out_stringSymbol = new FunctionSymbol("out_string", TypeSymbol.IO);
            // out_stringSymbol.setType(ce tip???); -- SELF_TYPE
            TypeSymbol.IO.add(out_stringSymbol);

            FunctionSymbol out_intSymbol = new FunctionSymbol("out_int", TypeSymbol.IO);
            // out_intSymbol.setType(ce tip???); -- SELF_TYPE
            TypeSymbol.IO.add(out_intSymbol);

            FunctionSymbol in_stringSymbol = new FunctionSymbol("in_string", TypeSymbol.IO);
            in_stringSymbol.setType(TypeSymbol.IO);
            TypeSymbol.IO.add(in_stringSymbol);

            FunctionSymbol in_intSymbol = new FunctionSymbol("in_int", TypeSymbol.IO);
            in_intSymbol.setType(TypeSymbol.INT);
            TypeSymbol.IO.add(in_intSymbol);
        }

        globals.add(TypeSymbol.INT);
        globals.add(TypeSymbol.BOOL);
        globals.add(TypeSymbol.STRING);
        globals.add(TypeSymbol.IO);
        globals.add(TypeSymbol.OBJECT);

    }
    
    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
