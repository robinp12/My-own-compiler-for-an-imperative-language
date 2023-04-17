package compiler.Parser.AST;

import compiler.Semantic.SemanticAnalyzer;

public class ExpressionNode{
    private String type;
    // Define common methods for all AST node classes here
    public ExpressionNode(String type){
        this.type = type;
    }

    public String getTypeStr(){
        return type;
    }

    public void accept(SemanticAnalyzer analyzer) {
        analyzer.visit(this);
    }

}
