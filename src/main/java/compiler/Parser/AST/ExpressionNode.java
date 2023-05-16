package compiler.Parser.AST;

public class ExpressionNode{
    private String type;
    // Define common methods for all AST node classes here
    public ExpressionNode(String type){
        this.type = type;
    }

    public String getTypeStr(){
        return type;
    }
}
