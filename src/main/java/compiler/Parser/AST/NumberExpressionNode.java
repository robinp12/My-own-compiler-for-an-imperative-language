package compiler.Parser.AST;

public class NumberExpressionNode extends ExpressionNode{
    public final int value;

    public NumberExpressionNode(int value){
        this.value = value;
    }
}
