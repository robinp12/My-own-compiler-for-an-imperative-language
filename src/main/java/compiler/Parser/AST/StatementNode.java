package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public abstract class StatementNode {
    private ExpressionNode expression;
    public StatementNode(ExpressionNode expression) {
        this.expression = expression;
    }
}
