package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;
import compiler.Parser.Parser;

import java.text.ParseException;

public class NumberNode extends ExpressionNode{
    private final String value;

    public NumberNode(String value){

        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static NumberNode parseNumber() throws ParseException {
        return new NumberNode(Parser.match(SymbolKind.NUM).getAttribute());
    }

    @Override
    public String toString() {
        return "NumberExpressionNode{" +
                "value='" + value + '\'' +
                '}';
    }
}
