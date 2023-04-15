package compiler.Parser.AST;

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
        switch (lookahead.kind){
            case DOUBLE:
                return new TypeNode(match(SymbolKind.DOUBLE).attribute);
            case INT:
                return new TypeNode(match(SymbolKind.INT).attribute);
            case STR:
                return new TypeNode(match(SymbolKind.STR).attribute);
            case BOOL:
                return new TypeNode(match(SymbolKind.BOOL).attribute);
            default:
                throw new ParseException("Invalid Type",0);
        }
    }
}