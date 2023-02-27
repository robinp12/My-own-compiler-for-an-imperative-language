package compiler.Lexer;

public enum SymbolKind {

    CONST("const"),
    RECORD("record"),
    VAR("var"),
    VAL("val"),
    PROC("proc"),
    FOR("for"),
    TO("to"),
    BY("by"),
    WHILE("while"),
    IF("if"),
    ELSE("else"),
    RETURN("return"),
    AND("and"),
    OR("or"),
    EQUALS("="),
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    PERC("%"),
    EQEQ("=="),
    DIFF("<>"),
    LESS("<"),
    MORE(">"),
    LESSEQ("<="),
    MOREEQ(">="),
    LPAR("("),
    RPAR(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACK("["),
    RBRACK("]"),
    DOT("."),
    SEMI(";"),
    COMA(",");



    public final String name;

    SymbolKind(String name) {
        this.name = name;
    }
}
