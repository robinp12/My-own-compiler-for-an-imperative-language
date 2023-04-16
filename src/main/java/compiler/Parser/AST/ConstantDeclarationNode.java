package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ConstantDeclarationNode extends ExpressionNode {

    private ExpressionNode assignment;
    public ConstantDeclarationNode(ExpressionNode assignment) {
        this.assignment = assignment;
    }

    public static ExpressionNode parseDeclarationConst() throws ParseException {
        match(SymbolKind.CONST);
        ExpressionNode assignment = AssignmentNode.parseAssignment();
        match(SymbolKind.SEMI);
        return new ConstantDeclarationNode(assignment);
    }

    public ExpressionNode getAssignment() {
        return assignment;
    }

    @Override
    public String toString() {
        return "ConstantDeclarationNode{" +
                "assignment=" + assignment +
                '}';
    }
}
