package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class IfStatementNode extends StatementNode{

    ExpressionNode condition;
    ArrayList<StatementNode> thenStatements;
    ArrayList<StatementNode> elseStatements;
    public IfStatementNode(ExpressionNode condition, ArrayList<StatementNode> thenStatements, ArrayList<StatementNode> elseStatements){
        super(condition);
        this.condition = condition;
        this.thenStatements = thenStatements;
        this.elseStatements = elseStatements;
    }

    public static IfStatementNode parseIfStatement() throws ParseException {
        match(SymbolKind.IF);
        match(SymbolKind.LITERAL);
        match(SymbolKind.EQEQ);
        match(SymbolKind.NUM);
        BlockNode.parseBlock();
        if(lookahead==null){
            return new IfStatementNode(null,null,null);
        }
        match(SymbolKind.ELSE);
        BlockNode.parseBlock();


        return new IfStatementNode(null,null,null);
    }
}
