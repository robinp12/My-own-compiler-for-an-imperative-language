package compiler.Lexer;

public class Symbol {
    private final SymbolKind kind;
    private final String attribute;

    public Symbol (SymbolKind kind, String attribute){
        this.kind = kind;
        this.attribute = attribute;
    }

    public Symbol (SymbolKind kind){
        this.kind = kind;
        this.attribute = "";
    }

    public SymbolKind getKind() {
        return kind;
    }

    public String getAttribute() {
        return attribute;
    }

    @Override public String toString ()
    {
        return "Symbol{kind = " + kind + ", attribute = '" + attribute + "'}";
    }
}
