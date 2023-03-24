package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

public class AssignmentNode extends ExpressionNode{
    // TODO

    private ExpressionNode left;
    private ExpressionNode right;

    public AssignmentNode(ExpressionNode left, ExpressionNode right) {
        this.left = left;
        this.right = right;
    }

    public AssignmentNode parseMethod() throws ParseException{
        return new AssignmentNode(left,right);
    }

}
