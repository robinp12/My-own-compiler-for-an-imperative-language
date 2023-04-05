package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class StatementNode extends ExpressionNode {
    private ExpressionNode expression;

    public StatementNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public static StatementNode parseStatement() throws ParseException{
        match(SymbolKind.RETURN);
        ExpressionNode expression = parseExpression();
        match(SymbolKind.SEMI);
        return new StatementNode(expression);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
