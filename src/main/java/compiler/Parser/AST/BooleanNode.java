package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;
import compiler.Parser.Parser;

import java.text.ParseException;

public class BooleanNode extends ExpressionNode{
    public final boolean val;


    public BooleanNode(boolean val) {
        this.val = val;
    }

    public static BooleanNode parseBoolean() throws ParseException{
        switch (Parser.lookahead.kind){
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
}
