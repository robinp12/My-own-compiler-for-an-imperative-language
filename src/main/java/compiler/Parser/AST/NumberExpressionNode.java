package compiler.Parser.AST;

public class NumberExpressionNode extends ExpressionNode{
    public final String value;

    public NumberExpressionNode(String value){
        super();
        this.value = value;
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
