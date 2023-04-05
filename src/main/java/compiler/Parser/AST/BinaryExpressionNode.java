package compiler.Parser.AST;

import compiler.Lexer.Symbol;

public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode left;
    private Symbol operator;
    private ExpressionNode right;

    public BinaryExpressionNode(ExpressionNode left, Symbol operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public Symbol getOperator() {
        return operator;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public <T> T accept(NodeVisitor visitor) {
        return visitor.visit(this);
    }

}
