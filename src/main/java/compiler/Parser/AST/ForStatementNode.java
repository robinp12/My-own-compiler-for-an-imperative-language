package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ForStatementNode extends ExpressionNode{
    private String variable;
    private ExpressionNode start;
    private ExpressionNode end;
    private ExpressionNode step;
    private BlockNode block;
    public ForStatementNode(String variable, ExpressionNode start, ExpressionNode end, ExpressionNode step, BlockNode block){
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.step = step;
        this.block = block;
    }
    public String getVariable() {
        return variable;
    }

    public ExpressionNode getStart() {
        return start;
    }

    public ExpressionNode getEnd() {
        return end;
    }

    public ExpressionNode getStep() {
        return step;
    }

    public BlockNode getBlock() {
        return block;
    }

    public static ExpressionNode parseForStatement() throws ParseException {
        match(SymbolKind.FOR);
        AssignmentNode assignmentNode = AssignmentNode.parseForLoopAssignment();
        String variable = assignmentNode.getIdentifier();
        ExpressionNode start = assignmentNode.getValue();

        match(SymbolKind.TO);
        ExpressionNode end = ValueNode.parseValue();
        if(lookahead.getKind() == SymbolKind.BY){
            match(SymbolKind.BY);
            ExpressionNode step = ValueNode.parseValue();
            BlockNode block = BlockNode.parseBlock();
            return new ForStatementNode(variable,start,end,step,block);
        }
        else {
            BlockNode block = BlockNode.parseBlock();
            return new ForStatementNode(variable,start,end,null,block);
        }

    }
    @Override
    public String toString() {
        return "ForStatementNode{" +
                "variable='" + variable + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", step=" + step +
                ", block=" + block +
                '}';
    }
}
