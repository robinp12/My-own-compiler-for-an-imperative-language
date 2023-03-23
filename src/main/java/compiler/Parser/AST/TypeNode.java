package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class TypeNode {
    private String identifier;

    public TypeNode(String identifier) throws ParseException {
        super();
        this.identifier = identifier;
    }

    public TypeNode parseType() throws ParseException {
        //TODO Only INT for now
        Symbol identifier = match(SymbolKind.INT);
        return new TypeNode(identifier.attribute);
    }
}
