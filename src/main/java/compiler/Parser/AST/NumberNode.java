package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;
import compiler.Parser.Parser;

import java.text.ParseException;

public class NumberNode extends ExpressionNode{
    private final String value;


    public NumberNode(String value, String type){
        super(type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static NumberNode parseNumber() throws ParseException {
        Symbol symbol = Parser.match(SymbolKind.NUM);
        if (isInt(symbol.getAttribute()))
            return new NumberNode(symbol.getAttribute(),"int");
        else {
            return new NumberNode(symbol.getAttribute(),"real");
        }
    }

    public static boolean isInt(String str) {
        try {
            return Float.parseFloat(str) % 1 == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public String toString() {
        return "NumberNode{" +
                "value='" + value + '\'' +
                '}';
    }
}
