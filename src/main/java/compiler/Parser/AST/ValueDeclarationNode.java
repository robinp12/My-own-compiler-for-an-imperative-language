package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ValueDeclarationNode extends ExpressionNode{
    private String name;
    private TypeNode type;
    private Symbol initialValue;

    public ValueDeclarationNode(){
    }

    public static ValueDeclarationNode parseDeclarationVal() throws ParseException {
        match(SymbolKind.VAL);
        AssignmentNode.parseAssignment();
        Symbol semi = match(SymbolKind.SEMI);
        return new ValueDeclarationNode();
    }

    @Override
    public String toString() {
        return "val " + name + " " + type + " = " + initialValue;
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
