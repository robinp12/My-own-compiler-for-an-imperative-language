package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ValDeclarationNode extends ExpressionNode {
    ExpressionNode assignment;

    public ValDeclarationNode(ExpressionNode assignment) {
        this.assignment = assignment;
    }

    public static ExpressionNode parseDeclarationVal() throws ParseException {
        match(SymbolKind.VAL);
        ExpressionNode assignment = AssignmentNode.parseAssignment();
        match(SymbolKind.SEMI);
        return new ValDeclarationNode(assignment);
    }
}
