package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class IfStatementNode extends ExpressionNode{

    private ExpressionNode condition;
    private BlockNode thenStatements;
    private BlockNode elseStatements;
    public IfStatementNode(ExpressionNode condition, BlockNode thenStatements, BlockNode elseStatements){
        this.condition = condition;
        this.thenStatements = thenStatements;
        this.elseStatements = elseStatements;
    }

    public static ExpressionNode parseIfStatement() throws ParseException {
        match(SymbolKind.IF);
        ExpressionNode condition =  BinaryExpressionNode.parseConditionNode();
        BlockNode thenBlock = BlockNode.parseBlock();

        if(lookahead.kind != SymbolKind.ELSE){
            return new IfStatementNode(condition,thenBlock,null);
        }
        match(SymbolKind.ELSE);
        BlockNode elseBlock = BlockNode.parseBlock();
        return new IfStatementNode(condition,thenBlock,elseBlock);
    }
}
