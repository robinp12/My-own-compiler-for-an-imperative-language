package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class AssignmentArrayNode extends ExpressionNode{

    private ExpressionNode size;
    private TypeNode type;

    public AssignmentArrayNode(ExpressionNode size, TypeNode type){
        super(type.getTypeSymbol());
        this.size = size;
        this.type = type;
    }
    public ExpressionNode getSize() {
        return size;
    }

    public TypeNode getType() {
        return type;
    }


    public static AssignmentArrayNode parseArrayDeclaration() throws ParseException {
        ExpressionNode size = null;
        TypeNode type = null;

        match(SymbolKind.LBRACK);
        match(SymbolKind.RBRACK);

        // If there is a default value
        if (lookahead.getKind() == SymbolKind.EQUALS) {
            match(SymbolKind.EQUALS);
            type = TypeNode.parseType();
            if(type.getTypeSymbol() != null){
                match(SymbolKind.LBRACK);
                match(SymbolKind.RBRACK);
                match(SymbolKind.LPAR);
                size = BinaryExpressionNode.parseBinaryExpressionNode(null);
                match(SymbolKind.RPAR);
            }
        }

        return new AssignmentArrayNode(size, type);
    }

    @Override
    public String toString() {
        return "AssignmentArrayNode{" +
                "size=" + size +
                ", type=" + type +
                '}';
    }
}
