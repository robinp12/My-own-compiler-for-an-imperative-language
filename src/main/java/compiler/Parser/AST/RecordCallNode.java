package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class RecordCallNode extends ExpressionNode {

    private String identifier;
    private RecordNode record;
    private ArrayList<ParamNode> parameters;

    public RecordCallNode(String name, RecordNode record, ArrayList<ParamNode> parameters) {
        super("record_call");
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

    public static RecordCallNode parseRecordCall() throws ParseException{
        // TODO
        String identifier = LiteralNode.parseLiteral().getLiteral();
        match(SymbolKind.LPAR);
        ArrayList<ParamNode> params = ParamListNode.parseParams();
        match(SymbolKind.RPAR);
        return new RecordCallNode(identifier,null,params);
    }

    @Override
    public String toString() {
        return "RecordCallNode{" +
                "name='" + identifier + '\'' +
                ", record=" + record +
                ", parameters=" + parameters +
                '}';
    }
}
