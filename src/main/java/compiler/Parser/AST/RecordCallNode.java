package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

public class RecordCallNode extends BasicASTNode{

    private RecordNode record;
    private ArrayList<ParamNode> parameters;

    public RecordCallNode(RecordNode record, ArrayList<ParamNode> parameters) throws ParseException {
        this.record = record;
        this.parameters = parameters;
    }

    public RecordCallNode parseRecordCall() throws ParseException{
        // TODO
        //String name = match(SymbolKind.LITERAL).string;
        match(SymbolKind.LPAR);
        ArrayList<ParamNode> params = null;
        match(SymbolKind.RPAR);
        return new RecordCallNode(null,null);
    }

}
