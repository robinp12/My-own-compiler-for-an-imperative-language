package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ConstantDeclarationNode extends ExpressionNode {

    ExpressionNode assignment;
    public ConstantDeclarationNode(ExpressionNode assignment) {
        this.assignment = assignment;
    }

    public static ExpressionNode parseDeclarationConst() throws ParseException {
        match(SymbolKind.CONST);
        ExpressionNode assignment = AssignmentNode.parseAssignment();
        match(SymbolKind.SEMI);
        return new ConstantDeclarationNode(assignment);
    }
}
