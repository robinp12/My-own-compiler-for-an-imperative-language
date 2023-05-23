package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class AssignmentArrayNode extends ExpressionNode{

    private NumberNode size;
    private TypeNode type;
    private ExpressionNode value;
    private ExpressionNode index;

    public AssignmentArrayNode(NumberNode size, TypeNode type, ExpressionNode value, ExpressionNode index){
        super(type.getTypeSymbol());
        this.size = size;
        this.type = type;
        this.value = value;
        this.index = index;
    }
    public NumberNode getSize() {
        return size;
    }

    public TypeNode getType() {
        return type;
    }
    public ExpressionNode getValue() {
        return value;
    }
    public ExpressionNode getIndex() {
        return index;
    }


    public static AssignmentArrayNode parseArrayDeclaration() throws ParseException {
        NumberNode size = null;
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
                size = NumberNode.parseNumber();
                match(SymbolKind.RPAR);
            }
        }

        return new AssignmentArrayNode(size, type, null,null);
    }

    @Override
    public String toString() {
        return "AssignmentArrayNode{" +
                "size=" + size +
                ", type=" + type +
                ", value=" + value +
                ", index=" + index +
                '}';
    }
}
