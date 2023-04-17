package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

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

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getThenStatements() {
        return thenStatements;
    }

    public BlockNode getElseStatements() {
        return elseStatements;
    }
    public static ExpressionNode parseIfStatement() throws ParseException {
        match(SymbolKind.IF);
        ExpressionNode condition =  BinaryExpressionNode.parseBinaryExpressionNode(null);
        BlockNode thenBlock = BlockNode.parseBlock();

        if(lookahead.getKind() != SymbolKind.ELSE){
            return new IfStatementNode(condition,thenBlock,null);
        }
        match(SymbolKind.ELSE);
        BlockNode elseBlock = BlockNode.parseBlock();
        return new IfStatementNode(condition,thenBlock,elseBlock);
    }

    @Override
    public String toString() {
        return "IfStatementNode{" +
                "condition=" + condition +
                ", thenStatements=" + thenStatements +
                ", elseStatements=" + elseStatements +
                '}';
    }
}
