package compiler.Parser.AST;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

public class BasicASTNode {
    public Symbol lookahead;
    private Lexer lexer;

    public BasicASTNode() throws ParseException {
        for (SymbolKind value : SymbolKind.values()) {
            match(value);
        }
    }

    public Symbol match(SymbolKind token) throws ParseException {
        if(lookahead.kind != token){
            throw new ParseException("No match",1);
        }
        else {
            Symbol matchingSymbol = lookahead;
            lookahead = lexer.getNextSymbol();
            return matchingSymbol;
        }
    }
}
