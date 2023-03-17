package compiler.Parser;

import compiler.Lexer.Symbol;

public class BasicASTNode {
    private Symbol symbol;
    private String value;

    public BasicASTNode(Symbol symbol, String value) {
        this.symbol = symbol;
        this.value = value;
    }
}
