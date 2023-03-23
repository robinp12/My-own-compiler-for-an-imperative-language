package compiler.Lexer;

public class Symbol {
    public final SymbolKind kind;
    public final String attribute;

    public Symbol (SymbolKind kind, String attribute){
        this.kind = kind;
        this.attribute = attribute;
    }

    public Symbol (SymbolKind kind){
        this.kind = kind;
        this.attribute = "";
    }

    @Override public String toString ()
    {
        return "Symbol{kind = " + kind + ", attribute = '" + attribute + "'}";
    }
}
