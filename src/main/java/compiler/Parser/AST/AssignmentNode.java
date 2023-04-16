package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;


public class AssignmentNode extends ExpressionNode{
    // TODO

    private String identifier;
    private TypeNode type;
    private ExpressionNode value;

    public AssignmentNode(String identifier, TypeNode type, ExpressionNode value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }
    public TypeNode getType() {
        return type;
    }

    public ExpressionNode getValue() {
        return value;
    }

    public static AssignmentNode parseAssignment() throws ParseException{
        String identifier = match(SymbolKind.LITERAL).getAttribute();
        TypeNode type = TypeNode.parseType();
        ExpressionNode value = null;
        if (lookahead.getKind() == SymbolKind.EQUALS){
            match(SymbolKind.EQUALS);
            value = ValueNode.parseValue();
        } else if (lookahead.getKind() == SymbolKind.LBRACK) { // in case of array
            value = AssignmentArrayNode.parseArrayDeclaration();
        }
        return new AssignmentNode(identifier, type, value);
    }

    @Override
    public String toString() {
        return "AssignmentNode{" +
                "identifier='" + identifier + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
