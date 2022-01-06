parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program
    :   (cls SEMI)+
    ;

cls : CLASS (type=TYPE_ID | type=SELF_TYPE) (INHERITS (parent=TYPE_ID | parent=SELF_TYPE))? LBRACE (feature SEMI)* RBRACE
;

feature : id=(ID | SELF) LPAREN (formal (COMMA formal)*)? RPAREN TWO_POINTS TYPE_ID LBRACE ex=expr RBRACE                 # method
        | formal (ASSIGN ex=expr)?                                                                   # member
;


formal_init : formal (ASSIGN ex=expr)? ;
formal : id=(ID | SELF) TWO_POINTS type=(TYPE_ID | SELF_TYPE) ;
case_branch : ID TWO_POINTS TYPE_ID RESULT result=expr;

expr :
  ex=expr (AROND TYPE_ID)? POINT ID LPAREN (expr (COMMA expr)*)? RPAREN                              # call_explicit
 | ID LPAREN (expr (COMMA expr)*)? RPAREN                                                            # call_implicit
 | IF cond=expr THEN thenBranch=expr ELSE elseBranch=expr FI                                         # if
 | WHILE cond=expr LOOP body=expr POOL                                                               # while
 | LBRACE (expr SEMI)+ RBRACE                                                                        # block
 | LET formal_init (COMMA formal_init)* IN body=expr                                                 # let
 | CASE ex=expr OF (case_branch SEMI)+ ESAC                                                          # case
 | NEW TYPE_ID                                                                                       # new
 | ISVOID ex=expr                                                                                    # isvoid
 | left=expr op=(MULT | DIV) right=expr                                                              # multDiv
 | left=expr op=(PLUS | MINUS) right=expr                                                            # plusMinus
 | left=expr op=(LT | LE | EQUAL) right=expr                                                         # relational
 | ID ASSIGN ex=expr                                                                                 # assign
 | COMPLEMENT ex=expr                                                                                # complement
 | NOT ex=expr                                                                                       # not
 | LPAREN ex=expr RPAREN                                                                             # paren
 | SELF                                                                                              # self
 | ID                                                                                                # id
 | INT                                                                                               # int
 | BOOL                                                                                              # bool
 | STRING                                                                                            # string
 ;
