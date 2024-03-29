package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.*;

import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
    private static Lexer lexer;
    public static Symbol lookahead;

    public Parser(Lexer lexer) throws ParseException {
        this.lexer = lexer;
        this.lookahead = lexer.getNextSymbol();
    }

    public static boolean NotAtEnd() {
        return lookahead != null;
    }

    /* Must return root of AST */
    public ProgramNode getAST() throws ParseException {
        return new ProgramNode(StatementNode.parseStatement().getStatements());
    }

    public static Symbol match(SymbolKind token) throws ParseException {
        if (lookahead == null) return null;
        if (lookahead.getKind() != token) {
            throw new ParseException("There is an error in parsing. " +
                    token + " token was expected but was " + lookahead.getKind() + "\n", 0);
        } else {
            Symbol matchingSymbol = lookahead;
            lookahead = lexer.getNextSymbol();
            return matchingSymbol;
        }
    }
}