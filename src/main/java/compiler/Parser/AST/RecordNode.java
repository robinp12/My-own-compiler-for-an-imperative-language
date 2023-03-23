package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class RecordNode {

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
        String name = match(SymbolKind.LITERAL).attribute;
        match(SymbolKind.LBRACE);
        ArrayList<ParamNode> params = null;
        match(SymbolKind.RBRACE);
        return new RecordNode(name,params);
    }

}
