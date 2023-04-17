package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ValueNode<T> extends ExpressionNode {
    private final T value;

    public ValueNode(T value) {
        super(null);
        this.value = value;
    }
    public T getValue() {
        return value;
    }

    public static ExpressionNode parseValue() throws ParseException{
        switch (lookahead.getKind()){
            case NUM:
                return new ValueNode<>(NumberNode.parseNumber());
            case TRUE: case FALSE:
                return new ValueNode<>(BooleanNode.parseBoolean());
            case STRING:
                return new ValueNode<>(StringNode.parseString());
            default:
                throw new ParseException("Error during value parsing",-1);
        }
    }

    @Override
    public String toString() {
        return "ValueNode{" +
                "value=" + value +
                '}';
    }
}
