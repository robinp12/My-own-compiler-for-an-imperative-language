package compiler.Parser.AST;

public abstract class ExpressionNode {
    // Define common methods for all AST node classes here

    public static ExpressionNode parseExpression(){
        return null;
    }
    public abstract <T> T accept(NodeVisitor visitor);

}
