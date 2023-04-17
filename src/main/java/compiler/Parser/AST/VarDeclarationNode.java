package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class VarDeclarationNode extends ExpressionNode {
    private ExpressionNode assignment;
    public VarDeclarationNode(ExpressionNode assignment) {
        super(assignment.getTypeStr());
        this.assignment = assignment;
    }
    public ExpressionNode getAssignment() {
        return assignment;
    }

    public static ExpressionNode parseDeclarationVar() throws ParseException {
        match(SymbolKind.VAR);
        ExpressionNode assignment = AssignmentNode.parseAssignment();
        match(SymbolKind.SEMI);
        return new VarDeclarationNode(assignment);
    }

    @Override
    public String toString() {
        return "VarDeclarationNode{" +
                "assignment=" + assignment +
                '}';
    }
}
