package compiler.Parser.AST;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends ExpressionNode {
    // AST Entry
    private final List<ExpressionNode> expressions;

    public ProgramNode(List<ExpressionNode> expressions) {
        this.expressions = expressions;
    }

    public List<ExpressionNode> getExpressions(){
        return this.expressions;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
