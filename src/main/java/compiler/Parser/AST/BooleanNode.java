package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;
import compiler.Parser.Parser;

import java.text.ParseException;

public class BooleanNode extends ExpressionNode{
    public final boolean val;

    public BooleanNode(boolean val) {
        super("bool");
        this.val = val;
    }
    public String getType(){
        return type;
    }
    public boolean isVal() {
        return val;
    }
    public static BooleanNode parseBoolean() throws ParseException{
        switch (Parser.lookahead.getKind()){
            case TRUE:
                Parser.match(SymbolKind.TRUE);
                return new BooleanNode(true);
            case FALSE:
                Parser.match(SymbolKind.FALSE);
                return new BooleanNode(false);
            default:
                throw new ParseException("Error parsing Boolean",-1);
        }
    }
    @Override
    public String toString() {
        return "BooleanNode{" +
                "val=" + val +
                '}';
    }
}
