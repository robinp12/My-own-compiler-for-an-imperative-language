package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ConstantDeclarationNode extends ExpressionNode {
    private AssignmentNode assignment;
    public ConstantDeclarationNode(AssignmentNode assignment) {
        super(assignment.getTypeStr());
        this.assignment = assignment;
    }

    public static ExpressionNode parseDeclarationConst() throws ParseException {
        match(SymbolKind.CONST);
        AssignmentNode assignment = AssignmentNode.parseAssignment();
        match(SymbolKind.SEMI);
        return new ConstantDeclarationNode(assignment);
    }

    public AssignmentNode getAssignment() {
        return assignment;
    }

    @Override
    public String toString() {
        return "ConstantDeclarationNode{" +
                "assignment=" + assignment +
                '}';
    }
}
