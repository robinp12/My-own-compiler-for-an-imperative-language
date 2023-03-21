package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

public class TypeNode extends BasicASTNode {
    private String identifier;

    public TypeNode(String identifier) throws ParseException {
        super();
        this.identifier = identifier;
    }

    public TypeNode parseType() throws ParseException {
        Symbol identifier = match(SymbolKind.INT);
        return new TypeNode(identifier.string);
    }
}
