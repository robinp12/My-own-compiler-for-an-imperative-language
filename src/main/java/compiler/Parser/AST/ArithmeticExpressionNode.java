package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ArithmeticExpressionNode extends ExpressionNode {

    private ExpressionNode leftOperand;
    private Symbol operator;
    private ExpressionNode rightOperand;

    /*ArithmeticExpressionNode is a specific type of BinaryExpressionNode that is used to represent arithmetic operations, such as addition, subtraction, multiplication, and division.
     It typically has two child nodes: a left operand and a right operand, which can themselves be expressions.
     */

    public ArithmeticExpressionNode(ExpressionNode leftOperand, Symbol operator, ExpressionNode rightOperand){
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public static ArithmeticExpressionNode parseArithmeticExpression() throws ParseException {
        ExpressionNode left = term();
        if(lookahead.getKind() == SymbolKind.PLUS || lookahead.getKind() == SymbolKind.MINUS){
            Symbol operator = match(lookahead.getKind());
            ExpressionNode right = term();
            return new ArithmeticExpressionNode(left,operator,right);
        }
        return null;
    }


    // Règle pour un terme arithmétique simple (multiplication et division uniquement)
    public static ExpressionNode term() throws ParseException {
        ExpressionNode left = factor();
        while (lookahead.getKind() == SymbolKind.STAR || lookahead.getKind() == SymbolKind.SLASH) {
            Symbol operator = match(lookahead.getKind()); // match le prochain jeton attendu (TIMES ou DIVIDE)
            ExpressionNode right = factor();
            // Créer un nouveau nœud d'expression pour représenter l'opération
            left = new ArithmeticExpressionNode(left,operator,right);
        }
        return left;
    }

    // Règle pour un facteur (un nombre ou une expression parenthésée)
    public static ExpressionNode factor() throws ParseException {
        if (lookahead.getKind() == SymbolKind.LITERAL){
            return LiteralNode.parseLiteral();
        }
        if (lookahead.getKind() == SymbolKind.NUM) {
            // Créer un nouveau nœud d'expression pour représenter le nombre
            return NumberNode.parseNumber();
        } else if (lookahead.getKind() == SymbolKind.DOUBLE) {
            // Créer un nouveau nœud d'expression pour représenter le nombre
            return NumberNode.parseNumber();
        } else if (lookahead.getKind() == SymbolKind.LPAR) {
            match(SymbolKind.LPAR); // match le prochain jeton attendu (LEFT_PAREN)
            ExpressionNode expression = parseArithmeticExpression(); // analyser l'expression à l'intérieur des parenthèses
            match(SymbolKind.RPAR); // match le prochain jeton attendu (RIGHT_PAREN)
            return expression;
        } else {
            throw new ParseException("Unexpected token: " + lookahead, 0);
        }
    }

    @Override
    public String toString() {
        return "ArithmeticExpressionNode{" +
                "leftOperand=" + leftOperand +
                ", operator=" + operator +
                ", rightOperand=" + rightOperand +
                '}';
    }
}
