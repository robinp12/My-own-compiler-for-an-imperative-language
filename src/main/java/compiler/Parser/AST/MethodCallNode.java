package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class MethodCallNode extends ExpressionNode {

    private String name;
    private RecordNode record;
    private ArrayList<ParamNode> parameters;

    public MethodCallNode(String name, RecordNode record, ArrayList<ParamNode> parameters) {
        this.name = name;
        this.record = record;
        this.parameters = parameters;
    }

    public static MethodCallNode parseMethodCall() throws ParseException{
        // TODO
        String name = match(SymbolKind.LITERAL).attribute;
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
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
