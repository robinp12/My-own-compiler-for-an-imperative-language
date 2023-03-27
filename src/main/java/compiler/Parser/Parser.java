package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.ExpressionNode;
import compiler.Parser.AST.Program;

import java.text.ParseException;

public class Parser {
    private static Lexer lexer;
    public static Symbol lookahead;

    public Parser(Lexer lexer) throws ParseException {
        this.lexer = lexer;
        this.lookahead = lexer.getNextSymbol();
    }

    /* Must return root of AST */
    public ExpressionNode getAST(){
        List<ExpressionNode> expressions = new ArrayList<>();

        while (!isAtEnd()) {
            try {
                ExpressionNode expr = parseExpr();
                expressions.add(expr);
            } catch (SyntaxErrorException e) {
                // Report the error and continue parsing
                System.err.println(e.getMessage());
                synchronize();
            }
            //this.lookahead = lexer.getNextSymbol();
        }

        return new ProgramNode(expressions);
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

    private boolean isAtEnd() {
        return lookahead.equals(null);
    }

    private parseExpr(){
        
    }
}