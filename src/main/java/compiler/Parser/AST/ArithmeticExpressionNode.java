package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ArithmeticExpressionNode extends ExpressionNode {

    private Symbol leftOperand;
    private String operator;
    private Symbol rightOperand;

    public ArithmeticExpressionNode(Symbol leftOperand, String operator, Symbol rightOperand){
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public static BinaryExpressionNode parseArithmeticExpression() throws ParseException {
        ExpressionNode left = term();
        if(lookahead.kind == SymbolKind.PLUS || lookahead.kind == SymbolKind.MINUS){
            Symbol operator = match(lookahead.kind);
            ExpressionNode right = term();
            return new BinaryExpressionNode(left,operator,right);
        }
        return null;
    }


    // Règle pour un terme arithmétique simple (multiplication et division uniquement)
    public static ExpressionNode term() throws ParseException {
        ExpressionNode left = factor();
        while (lookahead.kind == SymbolKind.STAR || lookahead.kind == SymbolKind.SLASH) {
            Symbol operator = match(lookahead.kind); // match le prochain jeton attendu (TIMES ou DIVIDE)
            ExpressionNode right = factor();
            // Créer un nouveau nœud d'expression pour représenter l'opération
            left = new BinaryExpressionNode(left,operator,right);
        }
        return left;
    }

    // Règle pour un facteur (un nombre ou une expression parenthésée)
    public static ExpressionNode factor() throws ParseException {
        if (lookahead.kind == SymbolKind.LITERAL){
            Symbol var = match(SymbolKind.LITERAL);
        }
        if (lookahead.kind == SymbolKind.NUM) {
            Symbol number = match(SymbolKind.NUM); // match le prochain jeton attendu (NUMBER)
            // Créer un nouveau nœud d'expression pour représenter le nombre
            return new NumberExpressionNode(Integer.parseInt(number.attribute));
        } else if (lookahead.kind == SymbolKind.LPAR) {
            match(SymbolKind.LPAR); // match le prochain jeton attendu (LEFT_PAREN)
            ExpressionNode expression = parseArithmeticExpression(); // analyser l'expression à l'intérieur des parenthèses
            match(SymbolKind.RPAR); // match le prochain jeton attendu (RIGHT_PAREN)
            return expression;
        } else {
            throw new ParseException("Unexpected token: " + lookahead, 0);
        }
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
