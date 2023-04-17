package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;
import compiler.Parser.Parser;

import java.text.ParseException;

public class LiteralNode extends ExpressionNode {
    private final String literal;

    public LiteralNode(String literal) {
        super("str");
        this.literal = literal;
    }

    public static LiteralNode parseLiteral() throws ParseException{
        return new LiteralNode(Parser.match(SymbolKind.LITERAL).getAttribute());
    }
}
