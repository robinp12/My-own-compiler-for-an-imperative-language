package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class LiteralNode extends ExpressionNode {
    private final String literal;

    public LiteralNode(String literal) {
        super("str");
        this.literal = literal;
    }
    public String getLiteral() {
        return literal;
    }

    public static LiteralNode parseLiteral() throws ParseException{
        String identifier = match(SymbolKind.LITERAL).getAttribute();
        return new LiteralNode(identifier);
    }
}
