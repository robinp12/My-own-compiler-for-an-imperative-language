package compiler.Lexer;

public class Symbol {

    private char symbol;

    public Symbol(char symbol){
        this.symbol = symbol;
    }
    public char toChar() {
        return symbol;
    }
}
