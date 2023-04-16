package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ValDeclarationNode extends ExpressionNode {
    private ExpressionNode assignment;

    public ValDeclarationNode(ExpressionNode assignment) {
        this.assignment = assignment;
    }
    public ExpressionNode getAssignment() {
        return assignment;
    }

    public static ExpressionNode parseDeclarationVal() throws ParseException {
        match(SymbolKind.VAL);
        ExpressionNode assignment = AssignmentNode.parseAssignment();
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
