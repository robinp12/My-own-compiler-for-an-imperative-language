package compiler.Parser.AST;

public class NumberExpressionNode extends ExpressionNode{
    public final int value;

    public NumberExpressionNode(int value){
        super()
        this.value = value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
