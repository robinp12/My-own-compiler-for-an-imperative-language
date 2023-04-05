package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class TypeNode extends ExpressionNode {
    private final String typeSymbol;

    public TypeNode(String typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public static TypeNode parseType() throws ParseException {
        if(lookahead.kind==SymbolKind.INT){
            Symbol identifier = match(SymbolKind.INT);
            return new TypeNode(identifier.attribute);
        }
        if(lookahead.kind==SymbolKind.DOUBLE){
            Symbol identifier = match(SymbolKind.DOUBLE);
            return new TypeNode(identifier.attribute);
        }
        return null;
    }
    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}