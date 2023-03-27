package compiler.Parser.AST;

public class VariableDeclarationNode {
    private String name;
    private TypeNode type;
    private ExpressionNode initialValue;

    public VariableDeclarationNode(String name, TypeNode type, ExpressionNode initialValue){
        this.name = name;
        this.type = type;
        this.initialValue = initialValue;
    }

    @Override
    public String toString() {
        return "var " + name + " " + type + " = " + initialValue;
    }
}
