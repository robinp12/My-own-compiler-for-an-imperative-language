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
    private boolean NotAtEnd() {
        return lookahead != null;
    }

    /* Must return root of AST */
    public ExpressionNode getAST() throws ParseException {
        ArrayList<ExpressionNode> expressions = new ArrayList<>();

        while (NotAtEnd()){
            switch (lookahead.kind){
                case CONST:
                    expressions.add(ConstantDeclarationNode.parseDeclarationConst());
                    break;
                case VAR:
                    expressions.add(VarDeclarationNode.parseDeclarationVar());
                    break;
                case VAL:
                    expressions.add(ValDeclarationNode.parseDeclarationVal());
                    break;
                case PROC:
                    expressions.add(MethodNode.parseMethod());
                    break;
                case IF:
                    expressions.add(IfStatementNode.parseIfStatement());
                    break;
                case WHILE:
                    expressions.add(WhileStatementNode.parseWhileStatement());
                    break;
                case FOR:
                    expressions.add(ForStatementNode.parseForStatement());
                    break;
                case RECORD:
                    expressions.add(RecordNode.parseRecord());
                    break;
                default:
                    throw new ParseException("Error during parsing: illegal symbol" + lookahead,-1);
            }
        }
        return new ProgramNode(expressions);
    }

    public static Symbol match(SymbolKind token) throws ParseException {
        if(lookahead == null) return null;
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