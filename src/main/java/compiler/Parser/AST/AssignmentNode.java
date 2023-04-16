package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;


public class AssignmentNode extends StatementNode{
    // TODO

    private String identifier;
    private TypeNode type;
    private ValueNode value;

    public AssignmentNode(String identifier, TypeNode type, ValueNode value) {
        super(null);
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

    public ValueNode getValue() {
        return value;
    }

    public static AssignmentNode parseAssignment() throws ParseException{
        String identifier = match(SymbolKind.LITERAL).attribute;
        TypeNode type = TypeNode.parseType();
        ValueNode value = null;
        if (lookahead.kind == SymbolKind.EQUALS){
            match(SymbolKind.EQUALS);
            value = ValueNode.parseValue();
        }
        return new AssignmentNode(identifier, type, value);
    }

}
