package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.ExpressionNode;
import compiler.Parser.AST.Program;

import java.text.ParseException;

public class Parser {
    private static Program root;
    private static Lexer lexer;
    public static Symbol lookahead;

    public Parser(Lexer lexer) throws ParseException {
        Parser.lexer = lexer;
        lookahead = lexer.getNextSymbol();
        root = new Program();
    }

    /* Must return root of AST */
    public Symbol getAST(){
        return null;
    }

    public static Symbol match(SymbolKind token) throws ParseException {
        if(lookahead.kind != token){
            throw new ParseException("No match, following is " + lookahead.kind + " but match is token " + token,0);
        }
        else {
            System.out.println("(Optional message) Matching Symbol " + token);
            Symbol matchingSymbol = lookahead;
            lookahead = lexer.getNextSymbol();
            return matchingSymbol;
        }
    }
}