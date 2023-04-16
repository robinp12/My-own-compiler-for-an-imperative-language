package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class AssignmentArrayNode extends ExpressionNode{

    private ExpressionNode size;
    private TypeNode type;

    public AssignmentArrayNode(ExpressionNode size, TypeNode type){
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
        if (lookahead.kind == SymbolKind.EQUALS) {
            match(SymbolKind.EQUALS);
            type = TypeNode.parseType();
            if(type.getTypeSymbol() != null){
                match(SymbolKind.LBRACK);
                match(SymbolKind.RBRACK);
                match(SymbolKind.LPAR);
                size = ValueNode.parseValue();
                match(SymbolKind.RPAR);
            }
        }

        return new AssignmentArrayNode(size, type);
    }
}
