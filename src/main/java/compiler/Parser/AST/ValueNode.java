package compiler.Parser.AST;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;

public class ValueNode<T> extends ExpressionNode {
    private final T value;

    public ValueNode(T value) {

        this.value = value;
    }

    public static ValueNode parseValue() throws ParseException{
        switch (lookahead.kind){
            case NUM: case DOUBLE:
                return new ValueNode(ArithmeticExpressionNode.parseArithmeticExpression());

            case TRUE: case FALSE:
                return new ValueNode(BinaryExpressionNode.parseBinaryExpression());
            case STRING:
                return new ValueNode(StringNode.parseStringExpression());
            default:
                throw new ParseException("Error during value parsing",-1);
        }
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
