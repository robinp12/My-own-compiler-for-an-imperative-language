package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class VarDeclarationNode extends ExpressionNode {
    private AssignmentNode assignment;
    public VarDeclarationNode(AssignmentNode assignment) {
        super(assignment.getTypeStr());
        this.assignment = assignment;
    }
    public AssignmentNode getAssignment() {
        return assignment;
    }

    public static VarDeclarationNode parseDeclarationVar() throws ParseException {
        match(SymbolKind.VAR);
        LiteralNode literal = LiteralNode.parseLiteral();
        AssignmentNode assignment = AssignmentNode.parseAssignment(literal.getLiteral());
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
