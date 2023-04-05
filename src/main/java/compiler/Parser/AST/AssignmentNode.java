package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;
import static compiler.Parser.Parser.match;


public class AssignmentNode extends ExpressionNode{
    // TODO

    private Symbol identifier;
    private TypeNode type;
    private Symbol value;

    public AssignmentNode(Symbol identifier, TypeNode type, Symbol value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public static AssignmentNode parseAssignment() throws ParseException{
        Symbol identifier = match(SymbolKind.LITERAL);
        TypeNode type = TypeNode.parseType();
        match(SymbolKind.EQUALS);
        Symbol value = match(SymbolKind.NUM);
        return new AssignmentNode(identifier, type, value);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
