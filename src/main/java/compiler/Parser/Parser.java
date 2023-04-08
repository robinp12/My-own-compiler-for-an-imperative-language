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
    private boolean isAtEnd() {
        return lookahead.equals(null);
    }

    /* Must return root of AST */
    public ExpressionNode getAST() throws ParseException {
        ArrayList<ExpressionNode> expressions = new ArrayList<>();

        SymbolKind nextSymbol = lookahead.kind;
        switch (nextSymbol){
            case CONST:
                ConstantDeclarationNode conststmts = ConstantDeclarationNode.parseDeclarationConst();
                break;
            case VAR:
                VariableDeclarationNode var = VariableDeclarationNode.parseDeclarationVar();
                break;
            case VAL:
                ValueDeclarationNode val = ValueDeclarationNode.parseDeclarationVal();
                break;
            case PROC:
                MethodNode method = MethodNode.parseMethod();
                break;
            case IF:
                IfStatementNode ifStmt = IfStatementNode.parseIfStatement();
                break;
            case WHILE:
                WhileStatementNode whileStmt = WhileStatementNode.parseWhileStatement();
                break;
            case FOR:
                ForStatementNode forStmt = ForStatementNode.parseForStatement();
                break;
            case RECORD:
                RecordNode recordStmt = RecordNode.parseRecord();
                break;
        }
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