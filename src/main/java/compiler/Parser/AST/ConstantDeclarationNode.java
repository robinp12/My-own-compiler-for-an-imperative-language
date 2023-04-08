package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ConstantDeclarationNode {
    private String name;
    private TypeNode type;
    private Symbol initialValue;

    public ConstantDeclarationNode(){
    }

    public static ConstantDeclarationNode parseDeclarationConst() throws ParseException {
        match(SymbolKind.CONST);
        AssignmentNode.parseAssignment();
        //TODO
        Symbol semi = match(SymbolKind.SEMI);
        return new ConstantDeclarationNode();
    }
    @Override
    public String toString() {
        return "const " + name + " " + type + " = " + initialValue;
    }
}
