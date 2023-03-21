package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;

public class Parser {
    private Symbol root;

    public Parser(Lexer lexer) {
        this.root = lexer.getNextSymbol();
    }

    /* Must return root of AST */
    public Symbol getAST(){
        return root;
    }
}