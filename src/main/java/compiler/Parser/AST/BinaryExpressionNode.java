package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

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

    public static ExpressionNode parseBinaryExpressionNode(ExpressionNode l) throws ParseException {
        ExpressionNode left = l;
        Symbol operator = null;

        while (true) {

            if (left == null){
                left = getExpressionValueNode();
            }
            if (!isBinaryOperator(lookahead)) {
                break;
            }


            switch (lookahead.getKind()) {
                case EQEQ:
                    operator = match(SymbolKind.EQEQ);
                    break;
                case LESSEQ:
                    operator = match(SymbolKind.LESSEQ);
                    break;
                case MOREEQ:
                    operator = match(SymbolKind.MOREEQ);
                    break;
                case DIFF:
                    operator = match(SymbolKind.DIFF);
                    break;
                case LESS:
                    operator = match(SymbolKind.LESS);
                    break;
                case MORE:
                    operator = match(SymbolKind.MORE);
                    break;
                case PLUS:
                    operator = match(SymbolKind.PLUS);
                    break;
                case MINUS:
                    operator = match(SymbolKind.MINUS);
                    break;
                case STAR:
                    operator = match(SymbolKind.STAR);
                    break;
                case SLASH:
                    operator = match(SymbolKind.SLASH);
                    break;
                case PERC:
                    operator = match(SymbolKind.PERC);
                    break;
                case AND:
                    operator = match(SymbolKind.AND);
                    break;
                case OR:
                    operator = match(SymbolKind.OR);
                    break;
            }

            ExpressionNode right;
            right = getExpressionValueNode();
            while (isBinaryOperator(lookahead)) {
                right = parseBinaryExpressionNode(right);
            }

            left = new BinaryExpressionNode(left,operator,right);
        }
        return left;
    }

    private static ExpressionNode getExpressionValueNode() throws ParseException {
        ExpressionNode node = null;
        switch (lookahead.getKind()) {
            case NUM:
            case TRUE:
            case FALSE:
            case STRING:
                return ValueNode.parseValue();
            case LITERAL:
                return LiteralNode.parseLiteral();
            default:
                throw new ParseException("Error parsing Value in Binary Expression"+lookahead,-2);
        }
    }

    public static boolean isBinaryOperator(Symbol s){
        switch (s.getKind()){
            case EQEQ: case LESSEQ: case MOREEQ: case DIFF:
            case LESS: case MORE: case PLUS: case MINUS: case STAR:
            case SLASH: case PERC: case AND: case OR:
                return true;
            default: return false;
        }
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

    @Override
    public String toString() {
        return "BinaryExpressionNode{" +
                "left=" + left +
                ", operator=" + operator +
                ", right=" + right +
                '}';
    }
}
