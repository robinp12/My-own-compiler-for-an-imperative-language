package compiler.Lexer;

public enum SymbolKind {

    VOID("void"),
    INT("int"),
    DOUBLE("real"),
    NUM("number"),
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
    COMMENT("//"),
    PERC("%"),
    EQEQ("=="),
    DIFF("!="),
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
    COMA(","),
    LITERAL("literal"),
    STR("str"),
    STRING("string"),
    TRUE("true"),
    FALSE("false"),
    BOOL("bool"),
    ENDOFFILE("endOfFile");



    private final String name;

    SymbolKind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
