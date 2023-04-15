package compiler.Parser.AST;

import compiler.Lexer.Symbol;

import java.text.ParseException;

/*
BinaryExpressionNode, on the other hand, is a more general type of node that can be used to represent any binary operation, not just arithmetic.
It can represent logical operations, comparison operations, bitwise operations, and any other kind of binary operation that might be defined in a programming language.
Like ArithmeticExpressionNode, it typically has two child nodes: a left operand and a right operand.
 */

public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode left;
    private Symbol operator;
    private ExpressionNode right;

    public BinaryExpressionNode(ExpressionNode left, Symbol operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public static BinaryExpressionNode parseBinaryExpression() throws ParseException {
        //TODO (big big job)
        return null;
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
