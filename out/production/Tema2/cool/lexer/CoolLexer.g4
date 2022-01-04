lexer grammar CoolLexer;

tokens { ERROR }

@header{
    package cool.lexer;
}

@members {
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
    Integer length = 0;
    StringBuilder str = new StringBuilder("");
    boolean null_char_error = false;
}

WS
    :   [ \n\f\r\t]+ -> skip
    ;

fragment UPPERCASE: [A-Z];
fragment LOWERCASE: [a-z];
fragment DIGIT: [0-9];
fragment UNDERSCORE: '_';

// keywords
IF: ('i' | 'I') ('f' | 'F');
THEN: ('t' | 'T') ('h' | 'H') ('e' | 'E') ('n' | 'N');
ELSE: ('e' | 'E') ('l' | 'L') ('s' | 'S') ('e' | 'E');
FI: ('f' | 'F') ('i' | 'I');

CLASS: ('c' | 'C') ('l' | 'L') ('a' | 'A') ('s' | 'S') ('s' | 'S');
INHERITS: ('i' | 'I') ('n' | 'N') ('h' | 'H') ('e' | 'E') ('r' | 'R') ('i' | 'I') ('t' | 'T') ('s' | 'S');

NEW: ('n' | 'N') ('e' | 'E') ('w' | 'E');
LET: ('l' | 'L') ('e' | 'E') ('t' | 'T');
IN: ('i' | 'I') ('n' | 'N') ;

ISVOID: ('i' | 'I') ('s' | 'S') ('v' | 'V') ('o' | 'O') ('i' | 'I') ('d' | 'D');
WHILE: ('w' | 'W') ('h' | 'H') ('i' | 'I') ('l' | 'L') ('e' | 'E');
LOOP: ('l' | 'L') ('o' | 'O') ('o' | 'O') ('p' | 'P');
POOL: ('p' | 'P') ('o' | 'O') ('o' | 'O') ('l' | 'L');
CASE: ('c' | 'C') ('a' | 'A') ('s' | 'S') ('e' | 'E');
OF: ('o' | 'O') ('f' | 'F');
ESAC: ('e' | 'E') ('s' | 'S') ('a' | 'A') ('c' | 'C');

NOT: ('n' | 'N') ('o' | 'O') ('t' | 'T');


AROND: '@';
COMPLEMENT: '~';
RESULT: '=>';

// useful characters
SEMI : ';';
COMMA : ',';
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
POINT: '.';
TWO_POINTS: ':';

// arithmethic op
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';

// comparison op
ASSIGN : '<-';
EQUAL : '=';
LT : '<';
LE : '<=';
// special identifiers
SELF_TYPE: 'SELF_TYPE';
SELF: 'self';


fragment NEW_LINE :  '\r'? '\n';
fragment NULL_CHAR : '\u0000';

LINE_COMMENT
    : '--' .*? (NEW_LINE | EOF) -> skip
    ;

BLOCK_COMMENT
    : (('*)' {raiseError("Unmatched *)");}) | '(*'
      (BLOCK_COMMENT | . )*?
      ('*)' { skip(); } | EOF { raiseError("EOF in comment"); }))
    ;

// default types
INT : DIGIT+;

BOOL: TRUE | FALSE;
TRUE: 't' ('r' | 'R') ('u' | 'U') ('e' | 'E');
FALSE: 'f' ('a' | 'A') ('l' | 'L') ('s' | 'S');

STRING : '"' {length = 0; str.setLength(0);  null_char_error = false;}
(( NULL_CHAR {null_char_error = true; })
| ('\\t' {length++; str.append('\t');})
| ('\\b' {length++; str.append('\b');})
| ('\\f' {length++; str.append('\f');})
| ('\\n' {length++; str.append('\n');})
| ('\\r' {length++; str.append('\r');})
| ('\\' NEW_LINE {length++; str.append('\n');})
| ('\\"' {length++; str.append("\"");})
| ('\\' . { length++; str.append(getText().charAt(getText().length() - 1)); })
| (. { length++; str.append(getText().charAt(getText().length() - 1)); }))*?
( NEW_LINE { raiseError("Unterminated string constant"); }
| EOF { raiseError("EOF in string constant"); }
| ('"' { setText(str.toString()); if (null_char_error == true) { raiseError("String contains null character"); }
                                 if (length > 1024) { raiseError("String constant too long"); } }));

// identifiers
TYPE_ID : UPPERCASE ((LOWERCASE) | (UPPERCASE) | (DIGIT) | (UNDERSCORE))*;
ID : LOWERCASE ((LOWERCASE) | (UPPERCASE) | (DIGIT) | (UNDERSCORE))*;

INVALID : . { raiseError("Invalid character: " + getText()); };