package compiler.Parser.AST;

public class ArrayNode {

    private TypeNode type;
    private String identifier;
    int size;

    public ArrayNode(TypeNode type, String identifier){
        this.type = type;
        this.identifier = identifier;
    }

    public static ArrayNode parseArray(){
        return new ArrayNode(null,null);
    }
}
