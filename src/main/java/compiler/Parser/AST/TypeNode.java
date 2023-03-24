package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class TypeNode extends ExpressionNode {
    private String identifier;

    public TypeNode(String identifier) {
        this.identifier = identifier;
    }

    public static TypeNode parseType() throws ParseException {
        //TODO
        Symbol identifier = match(SymbolKind.LITERAL);
        return new TypeNode(identifier.attribute);
    }
}
