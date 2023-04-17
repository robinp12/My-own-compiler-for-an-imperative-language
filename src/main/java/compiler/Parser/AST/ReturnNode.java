package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ReturnNode extends ExpressionNode {
    private ExpressionNode value;

    public ReturnNode(ExpressionNode value){
        super(value.getTypeStr());
        this.value = value;
    }
    public ExpressionNode getValue() {
        return value;
    }

    public static ExpressionNode parseReturn() throws ParseException {
        match(SymbolKind.RETURN);
        ExpressionNode value = BinaryExpressionNode.parseBinaryExpressionNode(null);
        match(SymbolKind.SEMI);
        return new ReturnNode(value);
    }

    @Override
    public String toString() {
        return "ReturnNode{" +
                "value=" + value +
                '}';
    }
}
