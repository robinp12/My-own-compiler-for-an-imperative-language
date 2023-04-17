package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class MethodCallNode extends ExpressionNode {

    private String identifier;
    private RecordNode record;
    private ArrayList<ParamNode> parameters;

    public MethodCallNode(String name, RecordNode record, ArrayList<ParamNode> parameters) {
        super("todo");
        this.identifier = name;
        this.record = record;
        this.parameters = parameters;
    }

    public String getIdentifier() {
        return identifier;
    }

    public RecordNode getRecord() {
        return record;
    }

    public ArrayList<ParamNode> getParameters() {
        return parameters;
    }

    public static MethodCallNode parseMethodCall() throws ParseException{
        // TODO
        String name = match(SymbolKind.LITERAL).getAttribute();
        switch (name){
            case "readInt":
            case "readReal":
            case "readString":
            case "writeInt":
            case "writeReal":
            case "write":
            case "writeln":
        }
        match(SymbolKind.LPAR);
        ArrayList<ParamNode> params = ParamListNode.parseParams();
        match(SymbolKind.RPAR);
        return new MethodCallNode(name,null,params);
    }

    @Override
    public String toString() {
        return "MethodCallNode{" +
                "name='" + identifier + '\'' +
                ", record=" + record +
                ", parameters=" + parameters +
                '}';
    }
}
