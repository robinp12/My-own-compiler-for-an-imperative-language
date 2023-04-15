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

    public static ExpressionNode parseConditionNode() throws ParseException {
        Symbol operator = null;
        //TODO (big big job)
        String left = match(SymbolKind.LITERAL).attribute;
        switch (lookahead.kind){
            case EQEQ:
                operator = match(SymbolKind.EQEQ);
                break;
            case LESSEQ:
                operator = match(SymbolKind.LESSEQ);
                break;
            case MOREEQ:
                operator = match(SymbolKind.MOREEQ);
                break;
            case NOTEQ:
                operator = match(SymbolKind.NOTEQ);
                break;
            case LESS:
                operator = match(SymbolKind.LESS);
                break;
            case MORE:
                operator = match(SymbolKind.MORE);
                break;
        }
        String right = match(SymbolKind.NUM).attribute;
        return new BinaryExpressionNode(null, operator, null);
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

}
