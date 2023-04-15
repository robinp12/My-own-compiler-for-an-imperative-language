package compiler.Parser.AST;

public class NumberExpressionNode extends ExpressionNode{
    public final String value;

    public NumberExpressionNode(String value){
        this.value = value;
    }
}
