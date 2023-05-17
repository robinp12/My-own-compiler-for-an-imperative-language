package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class RecordCallNode extends ExpressionNode {

    private ExpressionNode value;
    private String field;

    public RecordCallNode(String field, ExpressionNode value) {
        super("record");
        this.value = value;
        this.field = field;
    }

    public static RecordCallNode parseRecordCall() throws ParseException {
        match(SymbolKind.DOT);
        String field = LiteralNode.parseLiteral().getLiteral();
        match(SymbolKind.EQUALS);
        ExpressionNode value = BinaryExpressionNode.parseBinaryExpressionNode(null);
        match(SymbolKind.SEMI);
        return new RecordCallNode(field,value);
    }

    public String getfield() {
        return field;
    }

    public ExpressionNode getvalue() {
        return value;
    }


    @Override
    public String toString() {
        return "RecordCallNode{" +
                "field='" + field + '\'' +
                ", value=" + value +
                '}';
    }
}
