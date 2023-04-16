package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ReturnNode extends ExpressionNode {
    private ValueNode value;

    public ReturnNode(ValueNode value){
        this.value = value;
    }

    public static ExpressionNode parseReturn() throws ParseException {
        match(SymbolKind.RETURN);
        ValueNode<ExpressionNode> value = ValueNode.parseValue();
        match(SymbolKind.SEMI);
        return new ReturnNode(value);
    }

}
