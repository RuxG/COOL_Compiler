// Generated from /home/student/Desktop/Tema2/Tema2/src/cool/lexer/CoolLexer.g4 by ANTLR 4.8

    package cool.lexer;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, WS=2, IF=3, THEN=4, ELSE=5, FI=6, CLASS=7, INHERITS=8, NEW=9, 
		LET=10, IN=11, ISVOID=12, WHILE=13, LOOP=14, POOL=15, CASE=16, OF=17, 
		ESAC=18, NOT=19, AROND=20, COMPLEMENT=21, RESULT=22, SEMI=23, COMMA=24, 
		LPAREN=25, RPAREN=26, LBRACE=27, RBRACE=28, POINT=29, TWO_POINTS=30, PLUS=31, 
		MINUS=32, MULT=33, DIV=34, ASSIGN=35, EQUAL=36, LT=37, LE=38, SELF_TYPE=39, 
		SELF=40, LINE_COMMENT=41, BLOCK_COMMENT=42, INT=43, BOOL=44, TRUE=45, 
		FALSE=46, STRING=47, TYPE_ID=48, ID=49, INVALID=50;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "UPPERCASE", "LOWERCASE", "DIGIT", "UNDERSCORE", "IF", "THEN", 
			"ELSE", "FI", "CLASS", "INHERITS", "NEW", "LET", "IN", "ISVOID", "WHILE", 
			"LOOP", "POOL", "CASE", "OF", "ESAC", "NOT", "AROND", "COMPLEMENT", "RESULT", 
			"SEMI", "COMMA", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "POINT", "TWO_POINTS", 
			"PLUS", "MINUS", "MULT", "DIV", "ASSIGN", "EQUAL", "LT", "LE", "SELF_TYPE", 
			"SELF", "NEW_LINE", "NULL_CHAR", "LINE_COMMENT", "BLOCK_COMMENT", "INT", 
			"BOOL", "TRUE", "FALSE", "STRING", "TYPE_ID", "ID", "INVALID"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, "'@'", "'~'", "'=>'", 
			"';'", "','", "'('", "')'", "'{'", "'}'", "'.'", "':'", "'+'", "'-'", 
			"'*'", "'/'", "'<-'", "'='", "'<'", "'<='", "'SELF_TYPE'", "'self'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERROR", "WS", "IF", "THEN", "ELSE", "FI", "CLASS", "INHERITS", 
			"NEW", "LET", "IN", "ISVOID", "WHILE", "LOOP", "POOL", "CASE", "OF", 
			"ESAC", "NOT", "AROND", "COMPLEMENT", "RESULT", "SEMI", "COMMA", "LPAREN", 
			"RPAREN", "LBRACE", "RBRACE", "POINT", "TWO_POINTS", "PLUS", "MINUS", 
			"MULT", "DIV", "ASSIGN", "EQUAL", "LT", "LE", "SELF_TYPE", "SELF", "LINE_COMMENT", 
			"BLOCK_COMMENT", "INT", "BOOL", "TRUE", "FALSE", "STRING", "TYPE_ID", 
			"ID", "INVALID"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	    private void raiseError(String msg) {
	        setText(msg);
	        setType(ERROR);
	    }
	    Integer length = 0;
	    StringBuilder str = new StringBuilder("");
	    boolean null_char_error = false;


	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 46:
			BLOCK_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 51:
			STRING_action((RuleContext)_localctx, actionIndex);
			break;
		case 54:
			INVALID_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void BLOCK_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			raiseError("Unmatched *)");
			break;
		case 1:
			 skip(); 
			break;
		case 2:
			 raiseError("EOF in comment"); 
			break;
		}
	}
	private void STRING_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			length = 0; str.setLength(0);  null_char_error = false;
			break;
		case 4:
			null_char_error = true; 
			break;
		case 5:
			length++; str.append('\t');
			break;
		case 6:
			length++; str.append('\b');
			break;
		case 7:
			length++; str.append('\f');
			break;
		case 8:
			length++; str.append('\n');
			break;
		case 9:
			length++; str.append('\r');
			break;
		case 10:
			length++; str.append('\n');
			break;
		case 11:
			length++; str.append("\"");
			break;
		case 12:
			 length++; str.append(getText().charAt(getText().length() - 1)); 
			break;
		case 13:
			 length++; str.append(getText().charAt(getText().length() - 1)); 
			break;
		case 14:
			 raiseError("Unterminated string constant"); 
			break;
		case 15:
			 raiseError("EOF in string constant"); 
			break;
		case 16:
			 setText(str.toString()); if (null_char_error == true) { raiseError("String contains null character"); }
			                                 if (length > 1024) { raiseError("String constant too long"); } 
			break;
		}
	}
	private void INVALID_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 17:
			 raiseError("Invalid character: " + getText()); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\64\u0196\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\6\2s\n\2\r\2\16\2t\3\2\3"+
		"\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36"+
		"\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3\'\3("+
		"\3(\3)\3)\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3-\5-"+
		"\u010c\n-\3-\3-\3.\3.\3/\3/\3/\3/\7/\u0116\n/\f/\16/\u0119\13/\3/\3/\5"+
		"/\u011d\n/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\7\60\u012a"+
		"\n\60\f\60\16\60\u012d\13\60\3\60\3\60\3\60\3\60\3\60\3\60\5\60\u0135"+
		"\n\60\5\60\u0137\n\60\3\61\6\61\u013a\n\61\r\61\16\61\u013b\3\62\3\62"+
		"\5\62\u0140\n\62\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\7\65\u0172\n\65\f\65\16"+
		"\65\u0175\13\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\5\65\u017e\n\65\3\66"+
		"\3\66\3\66\3\66\3\66\7\66\u0185\n\66\f\66\16\66\u0188\13\66\3\67\3\67"+
		"\3\67\3\67\3\67\7\67\u018f\n\67\f\67\16\67\u0192\13\67\38\38\38\5\u0117"+
		"\u012b\u0173\29\3\4\5\2\7\2\t\2\13\2\r\5\17\6\21\7\23\b\25\t\27\n\31\13"+
		"\33\f\35\r\37\16!\17#\20%\21\'\22)\23+\24-\25/\26\61\27\63\30\65\31\67"+
		"\329\33;\34=\35?\36A\37C E!G\"I#K$M%O&Q\'S(U)W*Y\2[\2]+_,a-c.e/g\60i\61"+
		"k\62m\63o\64\3\2\30\5\2\13\f\16\17\"\"\3\2C\\\3\2c|\3\2\62;\4\2KKkk\4"+
		"\2HHhh\4\2VVvv\4\2JJjj\4\2GGgg\4\2PPpp\4\2NNnn\4\2UUuu\4\2EEee\4\2CCc"+
		"c\4\2TTtt\4\2GGyy\4\2XXxx\4\2QQqq\4\2FFff\4\2YYyy\4\2RRrr\4\2WWww\2\u01ad"+
		"\2\3\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2"+
		"\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2"+
		"\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2"+
		"\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2"+
		"\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q"+
		"\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2"+
		"\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2"+
		"\2o\3\2\2\2\3r\3\2\2\2\5x\3\2\2\2\7z\3\2\2\2\t|\3\2\2\2\13~\3\2\2\2\r"+
		"\u0080\3\2\2\2\17\u0083\3\2\2\2\21\u0088\3\2\2\2\23\u008d\3\2\2\2\25\u0090"+
		"\3\2\2\2\27\u0096\3\2\2\2\31\u009f\3\2\2\2\33\u00a3\3\2\2\2\35\u00a7\3"+
		"\2\2\2\37\u00aa\3\2\2\2!\u00b1\3\2\2\2#\u00b7\3\2\2\2%\u00bc\3\2\2\2\'"+
		"\u00c1\3\2\2\2)\u00c6\3\2\2\2+\u00c9\3\2\2\2-\u00ce\3\2\2\2/\u00d2\3\2"+
		"\2\2\61\u00d4\3\2\2\2\63\u00d6\3\2\2\2\65\u00d9\3\2\2\2\67\u00db\3\2\2"+
		"\29\u00dd\3\2\2\2;\u00df\3\2\2\2=\u00e1\3\2\2\2?\u00e3\3\2\2\2A\u00e5"+
		"\3\2\2\2C\u00e7\3\2\2\2E\u00e9\3\2\2\2G\u00eb\3\2\2\2I\u00ed\3\2\2\2K"+
		"\u00ef\3\2\2\2M\u00f1\3\2\2\2O\u00f4\3\2\2\2Q\u00f6\3\2\2\2S\u00f8\3\2"+
		"\2\2U\u00fb\3\2\2\2W\u0105\3\2\2\2Y\u010b\3\2\2\2[\u010f\3\2\2\2]\u0111"+
		"\3\2\2\2_\u0136\3\2\2\2a\u0139\3\2\2\2c\u013f\3\2\2\2e\u0141\3\2\2\2g"+
		"\u0146\3\2\2\2i\u014b\3\2\2\2k\u017f\3\2\2\2m\u0189\3\2\2\2o\u0193\3\2"+
		"\2\2qs\t\2\2\2rq\3\2\2\2st\3\2\2\2tr\3\2\2\2tu\3\2\2\2uv\3\2\2\2vw\b\2"+
		"\2\2w\4\3\2\2\2xy\t\3\2\2y\6\3\2\2\2z{\t\4\2\2{\b\3\2\2\2|}\t\5\2\2}\n"+
		"\3\2\2\2~\177\7a\2\2\177\f\3\2\2\2\u0080\u0081\t\6\2\2\u0081\u0082\t\7"+
		"\2\2\u0082\16\3\2\2\2\u0083\u0084\t\b\2\2\u0084\u0085\t\t\2\2\u0085\u0086"+
		"\t\n\2\2\u0086\u0087\t\13\2\2\u0087\20\3\2\2\2\u0088\u0089\t\n\2\2\u0089"+
		"\u008a\t\f\2\2\u008a\u008b\t\r\2\2\u008b\u008c\t\n\2\2\u008c\22\3\2\2"+
		"\2\u008d\u008e\t\7\2\2\u008e\u008f\t\6\2\2\u008f\24\3\2\2\2\u0090\u0091"+
		"\t\16\2\2\u0091\u0092\t\f\2\2\u0092\u0093\t\17\2\2\u0093\u0094\t\r\2\2"+
		"\u0094\u0095\t\r\2\2\u0095\26\3\2\2\2\u0096\u0097\t\6\2\2\u0097\u0098"+
		"\t\13\2\2\u0098\u0099\t\t\2\2\u0099\u009a\t\n\2\2\u009a\u009b\t\20\2\2"+
		"\u009b\u009c\t\6\2\2\u009c\u009d\t\b\2\2\u009d\u009e\t\r\2\2\u009e\30"+
		"\3\2\2\2\u009f\u00a0\t\13\2\2\u00a0\u00a1\t\n\2\2\u00a1\u00a2\t\21\2\2"+
		"\u00a2\32\3\2\2\2\u00a3\u00a4\t\f\2\2\u00a4\u00a5\t\n\2\2\u00a5\u00a6"+
		"\t\b\2\2\u00a6\34\3\2\2\2\u00a7\u00a8\t\6\2\2\u00a8\u00a9\t\13\2\2\u00a9"+
		"\36\3\2\2\2\u00aa\u00ab\t\6\2\2\u00ab\u00ac\t\r\2\2\u00ac\u00ad\t\22\2"+
		"\2\u00ad\u00ae\t\23\2\2\u00ae\u00af\t\6\2\2\u00af\u00b0\t\24\2\2\u00b0"+
		" \3\2\2\2\u00b1\u00b2\t\25\2\2\u00b2\u00b3\t\t\2\2\u00b3\u00b4\t\6\2\2"+
		"\u00b4\u00b5\t\f\2\2\u00b5\u00b6\t\n\2\2\u00b6\"\3\2\2\2\u00b7\u00b8\t"+
		"\f\2\2\u00b8\u00b9\t\23\2\2\u00b9\u00ba\t\23\2\2\u00ba\u00bb\t\26\2\2"+
		"\u00bb$\3\2\2\2\u00bc\u00bd\t\26\2\2\u00bd\u00be\t\23\2\2\u00be\u00bf"+
		"\t\23\2\2\u00bf\u00c0\t\f\2\2\u00c0&\3\2\2\2\u00c1\u00c2\t\16\2\2\u00c2"+
		"\u00c3\t\17\2\2\u00c3\u00c4\t\r\2\2\u00c4\u00c5\t\n\2\2\u00c5(\3\2\2\2"+
		"\u00c6\u00c7\t\23\2\2\u00c7\u00c8\t\7\2\2\u00c8*\3\2\2\2\u00c9\u00ca\t"+
		"\n\2\2\u00ca\u00cb\t\r\2\2\u00cb\u00cc\t\17\2\2\u00cc\u00cd\t\16\2\2\u00cd"+
		",\3\2\2\2\u00ce\u00cf\t\13\2\2\u00cf\u00d0\t\23\2\2\u00d0\u00d1\t\b\2"+
		"\2\u00d1.\3\2\2\2\u00d2\u00d3\7B\2\2\u00d3\60\3\2\2\2\u00d4\u00d5\7\u0080"+
		"\2\2\u00d5\62\3\2\2\2\u00d6\u00d7\7?\2\2\u00d7\u00d8\7@\2\2\u00d8\64\3"+
		"\2\2\2\u00d9\u00da\7=\2\2\u00da\66\3\2\2\2\u00db\u00dc\7.\2\2\u00dc8\3"+
		"\2\2\2\u00dd\u00de\7*\2\2\u00de:\3\2\2\2\u00df\u00e0\7+\2\2\u00e0<\3\2"+
		"\2\2\u00e1\u00e2\7}\2\2\u00e2>\3\2\2\2\u00e3\u00e4\7\177\2\2\u00e4@\3"+
		"\2\2\2\u00e5\u00e6\7\60\2\2\u00e6B\3\2\2\2\u00e7\u00e8\7<\2\2\u00e8D\3"+
		"\2\2\2\u00e9\u00ea\7-\2\2\u00eaF\3\2\2\2\u00eb\u00ec\7/\2\2\u00ecH\3\2"+
		"\2\2\u00ed\u00ee\7,\2\2\u00eeJ\3\2\2\2\u00ef\u00f0\7\61\2\2\u00f0L\3\2"+
		"\2\2\u00f1\u00f2\7>\2\2\u00f2\u00f3\7/\2\2\u00f3N\3\2\2\2\u00f4\u00f5"+
		"\7?\2\2\u00f5P\3\2\2\2\u00f6\u00f7\7>\2\2\u00f7R\3\2\2\2\u00f8\u00f9\7"+
		">\2\2\u00f9\u00fa\7?\2\2\u00faT\3\2\2\2\u00fb\u00fc\7U\2\2\u00fc\u00fd"+
		"\7G\2\2\u00fd\u00fe\7N\2\2\u00fe\u00ff\7H\2\2\u00ff\u0100\7a\2\2\u0100"+
		"\u0101\7V\2\2\u0101\u0102\7[\2\2\u0102\u0103\7R\2\2\u0103\u0104\7G\2\2"+
		"\u0104V\3\2\2\2\u0105\u0106\7u\2\2\u0106\u0107\7g\2\2\u0107\u0108\7n\2"+
		"\2\u0108\u0109\7h\2\2\u0109X\3\2\2\2\u010a\u010c\7\17\2\2\u010b\u010a"+
		"\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010e\7\f\2\2\u010e"+
		"Z\3\2\2\2\u010f\u0110\7\2\2\2\u0110\\\3\2\2\2\u0111\u0112\7/\2\2\u0112"+
		"\u0113\7/\2\2\u0113\u0117\3\2\2\2\u0114\u0116\13\2\2\2\u0115\u0114\3\2"+
		"\2\2\u0116\u0119\3\2\2\2\u0117\u0118\3\2\2\2\u0117\u0115\3\2\2\2\u0118"+
		"\u011c\3\2\2\2\u0119\u0117\3\2\2\2\u011a\u011d\5Y-\2\u011b\u011d\7\2\2"+
		"\3\u011c\u011a\3\2\2\2\u011c\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u011f"+
		"\b/\2\2\u011f^\3\2\2\2\u0120\u0121\7,\2\2\u0121\u0122\7+\2\2\u0122\u0123"+
		"\3\2\2\2\u0123\u0137\b\60\3\2\u0124\u0125\7*\2\2\u0125\u0126\7,\2\2\u0126"+
		"\u012b\3\2\2\2\u0127\u012a\5_\60\2\u0128\u012a\13\2\2\2\u0129\u0127\3"+
		"\2\2\2\u0129\u0128\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u012c\3\2\2\2\u012b"+
		"\u0129\3\2\2\2\u012c\u0134\3\2\2\2\u012d\u012b\3\2\2\2\u012e\u012f\7,"+
		"\2\2\u012f\u0130\7+\2\2\u0130\u0131\3\2\2\2\u0131\u0135\b\60\4\2\u0132"+
		"\u0133\7\2\2\3\u0133\u0135\b\60\5\2\u0134\u012e\3\2\2\2\u0134\u0132\3"+
		"\2\2\2\u0135\u0137\3\2\2\2\u0136\u0120\3\2\2\2\u0136\u0124\3\2\2\2\u0137"+
		"`\3\2\2\2\u0138\u013a\5\t\5\2\u0139\u0138\3\2\2\2\u013a\u013b\3\2\2\2"+
		"\u013b\u0139\3\2\2\2\u013b\u013c\3\2\2\2\u013cb\3\2\2\2\u013d\u0140\5"+
		"e\63\2\u013e\u0140\5g\64\2\u013f\u013d\3\2\2\2\u013f\u013e\3\2\2\2\u0140"+
		"d\3\2\2\2\u0141\u0142\7v\2\2\u0142\u0143\t\20\2\2\u0143\u0144\t\27\2\2"+
		"\u0144\u0145\t\n\2\2\u0145f\3\2\2\2\u0146\u0147\7h\2\2\u0147\u0148\t\17"+
		"\2\2\u0148\u0149\t\f\2\2\u0149\u014a\t\r\2\2\u014ah\3\2\2\2\u014b\u014c"+
		"\7$\2\2\u014c\u0173\b\65\6\2\u014d\u014e\5[.\2\u014e\u014f\b\65\7\2\u014f"+
		"\u0172\3\2\2\2\u0150\u0151\7^\2\2\u0151\u0152\7v\2\2\u0152\u0153\3\2\2"+
		"\2\u0153\u0172\b\65\b\2\u0154\u0155\7^\2\2\u0155\u0156\7d\2\2\u0156\u0157"+
		"\3\2\2\2\u0157\u0172\b\65\t\2\u0158\u0159\7^\2\2\u0159\u015a\7h\2\2\u015a"+
		"\u015b\3\2\2\2\u015b\u0172\b\65\n\2\u015c\u015d\7^\2\2\u015d\u015e\7p"+
		"\2\2\u015e\u015f\3\2\2\2\u015f\u0172\b\65\13\2\u0160\u0161\7^\2\2\u0161"+
		"\u0162\7t\2\2\u0162\u0163\3\2\2\2\u0163\u0172\b\65\f\2\u0164\u0165\7^"+
		"\2\2\u0165\u0166\5Y-\2\u0166\u0167\b\65\r\2\u0167\u0172\3\2\2\2\u0168"+
		"\u0169\7^\2\2\u0169\u016a\7$\2\2\u016a\u016b\3\2\2\2\u016b\u0172\b\65"+
		"\16\2\u016c\u016d\7^\2\2\u016d\u016e\13\2\2\2\u016e\u0172\b\65\17\2\u016f"+
		"\u0170\13\2\2\2\u0170\u0172\b\65\20\2\u0171\u014d\3\2\2\2\u0171\u0150"+
		"\3\2\2\2\u0171\u0154\3\2\2\2\u0171\u0158\3\2\2\2\u0171\u015c\3\2\2\2\u0171"+
		"\u0160\3\2\2\2\u0171\u0164\3\2\2\2\u0171\u0168\3\2\2\2\u0171\u016c\3\2"+
		"\2\2\u0171\u016f\3\2\2\2\u0172\u0175\3\2\2\2\u0173\u0174\3\2\2\2\u0173"+
		"\u0171\3\2\2\2\u0174\u017d\3\2\2\2\u0175\u0173\3\2\2\2\u0176\u0177\5Y"+
		"-\2\u0177\u0178\b\65\21\2\u0178\u017e\3\2\2\2\u0179\u017a\7\2\2\3\u017a"+
		"\u017e\b\65\22\2\u017b\u017c\7$\2\2\u017c\u017e\b\65\23\2\u017d\u0176"+
		"\3\2\2\2\u017d\u0179\3\2\2\2\u017d\u017b\3\2\2\2\u017ej\3\2\2\2\u017f"+
		"\u0186\5\5\3\2\u0180\u0185\5\7\4\2\u0181\u0185\5\5\3\2\u0182\u0185\5\t"+
		"\5\2\u0183\u0185\5\13\6\2\u0184\u0180\3\2\2\2\u0184\u0181\3\2\2\2\u0184"+
		"\u0182\3\2\2\2\u0184\u0183\3\2\2\2\u0185\u0188\3\2\2\2\u0186\u0184\3\2"+
		"\2\2\u0186\u0187\3\2\2\2\u0187l\3\2\2\2\u0188\u0186\3\2\2\2\u0189\u0190"+
		"\5\7\4\2\u018a\u018f\5\7\4\2\u018b\u018f\5\5\3\2\u018c\u018f\5\t\5\2\u018d"+
		"\u018f\5\13\6\2\u018e\u018a\3\2\2\2\u018e\u018b\3\2\2\2\u018e\u018c\3"+
		"\2\2\2\u018e\u018d\3\2\2\2\u018f\u0192\3\2\2\2\u0190\u018e\3\2\2\2\u0190"+
		"\u0191\3\2\2\2\u0191n\3\2\2\2\u0192\u0190\3\2\2\2\u0193\u0194\13\2\2\2"+
		"\u0194\u0195\b8\24\2\u0195p\3\2\2\2\24\2t\u010b\u0117\u011c\u0129\u012b"+
		"\u0134\u0136\u013b\u013f\u0171\u0173\u017d\u0184\u0186\u018e\u0190\25"+
		"\b\2\2\3\60\2\3\60\3\3\60\4\3\65\5\3\65\6\3\65\7\3\65\b\3\65\t\3\65\n"+
		"\3\65\13\3\65\f\3\65\r\3\65\16\3\65\17\3\65\20\3\65\21\3\65\22\38\23";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}