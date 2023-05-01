package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;


public class AssignmentNode extends ExpressionNode{
    private String identifier;
    private TypeNode type;
    private ExpressionNode value;

    public AssignmentNode(String identifier, TypeNode type, ExpressionNode value) {
        super(type.getTypeSymbol());
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
       String identifier = LiteralNode.parseLiteral().getLiteral();
        ExpressionNode value = null;

        switch (lookahead.getKind()){
            case EQUALS:
                match(SymbolKind.EQUALS);
                value = BinaryExpressionNode.parseBinaryExpressionNode(null);
                match(SymbolKind.SEMI);
                return new AssignmentNode(identifier,new TypeNode(value.getTypeStr(),value.getTypeStr()),value);
            case LBRACK: // Assignment of value to existing array
                AssignmentArrayNode values = parseArrayAssignment();
                return new AssignmentNode(identifier,values.getType(),values);
        }

        // Declaration variable
        TypeNode type = TypeNode.parseType();
        switch (lookahead.getKind()){
            case LBRACK: // in case of array
                value = AssignmentArrayNode.parseArrayDeclaration();
                return new AssignmentNode(identifier, type, value);

            case EQUALS: // Assign value to variable
                match(SymbolKind.EQUALS);
                value = BinaryExpressionNode.parseBinaryExpressionNode(null);
                return new AssignmentNode(identifier, type, value);

            default:
                return new AssignmentNode(identifier,type,null);
        }
    }
    
    public static AssignmentArrayNode parseArrayAssignment() throws ParseException {
        match(SymbolKind.LBRACK);
        ExpressionNode index = BinaryExpressionNode.parseBinaryExpressionNode(null);
        match(SymbolKind.RBRACK);

        match(SymbolKind.EQUALS);
        ExpressionNode value = BinaryExpressionNode.parseBinaryExpressionNode(null);
        match(SymbolKind.SEMI);
        return new AssignmentArrayNode(null,new TypeNode(value.getTypeStr(),value.getTypeStr()), value, index);
    }

    public static AssignmentNode parseForLoopAssignment() throws ParseException{
        String identifier = LiteralNode.parseLiteral().getLiteral();
        TypeNode type = new TypeNode("int", identifier);
        match(SymbolKind.EQUALS);
        ExpressionNode value = BinaryExpressionNode.parseBinaryExpressionNode(null);

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
