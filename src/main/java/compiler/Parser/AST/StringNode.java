package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;
public class StringNode extends ExpressionNode {
    private final String value;

    public StringNode(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static StringNode parseString() throws ParseException {
        return new StringNode(match(SymbolKind.STRING).getAttribute());
    }

    @Override
    public String toString() {
        return "StringNode{" +
                "value='" + value + '\'' +
                '}';
    }
}
