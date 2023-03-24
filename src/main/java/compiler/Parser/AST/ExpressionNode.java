package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

public abstract class ExpressionNode{
    protected String type; // le type de l'expression

    protected int line; // la ligne dans le code source où se trouve l'expression

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
