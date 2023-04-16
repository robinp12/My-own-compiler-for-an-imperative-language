package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class WhileStatementNode extends ExpressionNode{
    private ExpressionNode condition;
    private BlockNode block;
    public WhileStatementNode(ExpressionNode condition, BlockNode block){
        this.condition = condition;
        this.block = block;
    }
    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getBlock() {
        return block;
    }

    public static ExpressionNode parseWhileStatement() throws ParseException {
        match(SymbolKind.WHILE);
        ExpressionNode condition = BinaryExpressionNode.parseConditionNode();
        BlockNode block = BlockNode.parseBlock();
        return new WhileStatementNode(condition, block);
    }
    @Override
    public String toString() {
        return "while (" + condition + ") " + block;
    }
}
