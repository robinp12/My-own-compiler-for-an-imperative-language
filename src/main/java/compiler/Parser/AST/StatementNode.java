package compiler.Parser.AST;

public class StatementNode extends ExpressionNode{
    public StatementNode() {
    }

    public static StatementNode parseStatement(){
        StatementNode stmt = new StatementNode();

        return stmt;
    }
}
