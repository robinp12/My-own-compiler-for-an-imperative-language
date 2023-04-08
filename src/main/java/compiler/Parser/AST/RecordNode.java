package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class RecordNode extends ExpressionNode {

    private String identifier;
    private TypeNode returnType;
    private ArrayList<ParamNode> parameters;
    private BlockNode body;

    public RecordNode(String identifier, ArrayList<ParamNode> parameters) {
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public static RecordNode parseRecord() throws ParseException{
        // TODO
        match(SymbolKind.RECORD);
        String name = match(SymbolKind.LITERAL).attribute;
        match(SymbolKind.LBRACE);
        //...
        match(SymbolKind.RBRACE);
        return new RecordNode(name,null);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
