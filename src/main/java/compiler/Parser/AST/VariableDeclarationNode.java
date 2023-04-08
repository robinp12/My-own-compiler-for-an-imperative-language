package compiler.Parser.AST;
import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import static compiler.Parser.Parser.match;

public class VariableDeclarationNode {
    private String name;
    private TypeNode type;
    private Symbol initialValue;

    public VariableDeclarationNode(){
    }

    public static VariableDeclarationNode parseDeclarationVar() throws ParseException {
        match(SymbolKind.VAR);
        AssignmentNode.parseAssignment();
        Symbol semi = match(SymbolKind.SEMI);
        return new VariableDeclarationNode();
    }
    @Override
    public String toString() {
        return "var " + name + " " + type + " = " + initialValue;
    }
}