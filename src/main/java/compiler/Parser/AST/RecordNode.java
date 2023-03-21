package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

public class RecordNode extends BasicASTNode{

    private String identifier;
    private TypeNode returnType;
    private ArrayList<ParamNode> parameters;
    private BlockNode body;

    public RecordNode(String identifier, ArrayList<ParamNode> parameters) throws ParseException {
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public RecordNode parseRecord() throws ParseException{
        // TODO
        String name = match(SymbolKind.LITERAL).string;
        match(SymbolKind.LBRACE);
        ArrayList<ParamNode> params = null;
        match(SymbolKind.RBRACE);
        return new RecordNode(name,params);
    }

}
