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
        if (isFloat(symbol.getAttribute()))
            return new NumberNode(symbol.getAttribute(),"real");
        else {
            return new NumberNode(symbol.getAttribute(),"int");
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
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
