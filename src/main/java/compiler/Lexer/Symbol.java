package compiler.Lexer;

public class Symbol {
    public final SymbolKind kind;
    public final String string;

    public Symbol (SymbolKind kind, String string){
        this.kind = kind;
        this.string = string;
    }

    public Symbol (SymbolKind kind){
        this.kind = kind;
        this.string = "";
    }

    @Override public String toString ()
    {
        return "Symbol{kind = " + kind + ", string = '" + string + "'}";
    }
}
