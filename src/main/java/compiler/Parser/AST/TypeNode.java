package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class TypeNode extends ExpressionNode {
    private String typeSymbol;
    private String name;

    public TypeNode(String typeSymbol, String name) {
        super(typeSymbol);
        this.typeSymbol = typeSymbol;
        this.name = name;
    }

    public static TypeNode parseType() throws ParseException {
        switch (lookahead.getKind()){
            case DOUBLE:
                return new TypeNode(SymbolKind.DOUBLE.getName(),match(SymbolKind.DOUBLE).getAttribute());
            case INT:
                return new TypeNode(SymbolKind.INT.getName(),match(SymbolKind.INT).getAttribute());
            case STR:
                return new TypeNode(SymbolKind.STR.getName(),match(SymbolKind.STR).getAttribute());
            case BOOL:
                return new TypeNode(SymbolKind.BOOL.getName(),match(SymbolKind.BOOL).getAttribute());
            default:
                throw new ParseException("Invalid Type" + lookahead.getKind(),0);
        }
    }

    public String getTypeSymbol() {
        return typeSymbol;
    }

    @Override
    public String toString() {
        return "TypeNode{" +
                "typeSymbol='" + typeSymbol + '\'' +
                '}';
    }
}