package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ValueNode<T> extends ExpressionNode {
    private final T value;

    public ValueNode(T value) {
        this.value = value;
    }
    public T getValue() {
        return value;
    }

    public static ValueNode<ExpressionNode> parseValue() throws ParseException{
        switch (lookahead.getKind()){
            case NUM: case DOUBLE:
                return new ValueNode<>(ArithmeticExpressionNode.parseArithmeticExpression());
            case TRUE: case FALSE:
                return new ValueNode<>(BooleanNode.parseBoolean());
            case STRING:
                return new ValueNode<>(StringNode.parseStringExpression());
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
