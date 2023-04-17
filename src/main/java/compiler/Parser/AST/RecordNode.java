package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class RecordNode extends ExpressionNode {

    private String identifier;
    private ArrayList<ParamNode> fields;

    public RecordNode(String identifier, ArrayList<ParamNode> fields) {
        super("record");
        this.identifier = identifier;
        this.fields = fields;
    }
    public String getIdentifier() {
        return identifier;
    }
    public String getType(){return type;}

    public ArrayList<ParamNode> getFields() {
        return fields;
    }

    public static RecordNode parseRecord() throws ParseException{
        match(SymbolKind.RECORD);
        String identifier = LiteralNode.parseLiteral().getLiteral();
        match(SymbolKind.LBRACE);
        ArrayList<ParamNode> fields = new ArrayList<>();
        while (lookahead.getKind() == SymbolKind.LITERAL) {
            fields.add(ParamNode.parseParam());
            match(SymbolKind.SEMI);
        }
        match(SymbolKind.RBRACE);
        return new RecordNode(identifier,fields);
    }

    @Override
    public String toString() {
        return "RecordNode{" +
                "identifier='" + identifier + '\'' +
                ", fields=" + fields +
                '}';
    }
}
