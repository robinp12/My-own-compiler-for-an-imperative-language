package compiler.Parser.AST;

import java.util.ArrayList;
import java.util.List;

public class Program {
    // AST Entry
    public ArrayList<ExpressionNode> expressions;
    public Program() {
        this.expressions = new ArrayList<>();
    }
    public void addExpression(ExpressionNode node){
        expressions.add(node);
    }
}
