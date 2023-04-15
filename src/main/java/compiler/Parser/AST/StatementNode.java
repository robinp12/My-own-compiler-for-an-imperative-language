package compiler.Parser.AST;

public abstract class StatementNode extends ExpressionNode {
    private ExpressionNode expression;
    public StatementNode(ExpressionNode expression) {
        this.expression = expression;
    }
}
