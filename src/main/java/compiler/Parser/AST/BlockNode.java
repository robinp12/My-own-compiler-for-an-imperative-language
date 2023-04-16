package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;


public class BlockNode extends ExpressionNode{
    private StatementNode statements;

    public BlockNode(StatementNode stmts) {
        this.statements = stmts;
    }

    public StatementNode getStatements() {
        return statements;
    }
    public static BlockNode parseBlock() throws ParseException {
        match(SymbolKind.LBRACE);
        if (lookahead.getKind() == SymbolKind.RBRACE){
            match(SymbolKind.RBRACE);
            return new BlockNode(null);
        }
        StatementNode statements = StatementNode.parseStatement();
        return new BlockNode(statements);
    }

    @Override
    public String toString() {
        return "BlockNode{" +
                "statements=" + statements +
                '}';
    }
}