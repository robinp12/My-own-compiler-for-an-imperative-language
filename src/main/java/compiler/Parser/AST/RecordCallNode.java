package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class RecordCallNode extends ExpressionNode {

    private RecordNode record;
    private ArrayList<ParamNode> parameters;

    public RecordCallNode(RecordNode record, ArrayList<ParamNode> parameters) {
        this.record = record;
        this.parameters = parameters;
    }

    public RecordCallNode parseRecordCall() throws ParseException{
        // TODO
        //String name = match(SymbolKind.RECORD).attribute;
        match(SymbolKind.LPAR);
        ArrayList<ParamNode> params = ParamListNode.parseParams();
        match(SymbolKind.RPAR);
        return new RecordCallNode(null,params);
    }

}
