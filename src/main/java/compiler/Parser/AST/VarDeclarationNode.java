package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class VarDeclarationNode extends ExpressionNode {
    ExpressionNode assignment;
    public VarDeclarationNode(ExpressionNode assignment) {
        this.assignment = assignment;
    }

    public static ExpressionNode parseDeclarationVar() throws ParseException {
        match(SymbolKind.VAR);
        ExpressionNode assignment = AssignmentNode.parseAssignment();
        match(SymbolKind.SEMI);
        return new VarDeclarationNode(assignment);
    }
}
