package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ValDeclarationNode extends ExpressionNode {
    private AssignmentNode assignment;

    public ValDeclarationNode(AssignmentNode assignment) {
        super(assignment.getTypeStr());
        this.assignment = assignment;
    }
    public AssignmentNode getAssignment() {
        return assignment;
    }

    public static ValDeclarationNode parseDeclarationVal() throws ParseException {
        match(SymbolKind.VAL);
        AssignmentNode assignment = AssignmentNode.parseAssignment();
        match(SymbolKind.SEMI);
        return new ValDeclarationNode(assignment);
    }

    @Override
    public String toString() {
        return "ValDeclarationNode{" +
                "assignment=" + assignment +
                '}';
    }
}
