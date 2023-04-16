package compiler.Parser.AST;

public class NumberExpressionNode extends ExpressionNode{
    private final String value;

    public NumberExpressionNode(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NumberExpressionNode{" +
                "value='" + value + '\'' +
                '}';
    }
}
