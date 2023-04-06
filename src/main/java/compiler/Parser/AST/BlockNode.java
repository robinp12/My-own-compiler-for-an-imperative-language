package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.AST.StatementListNode.parseStatements;
import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;


public class BlockNode extends ExpressionNode{
    private ArrayList<StatementNode> statements;

    public BlockNode(ArrayList<StatementNode> stmts) {
        statements = stmts;
    }

    public static BlockNode parseBlock() throws ParseException {
        match(SymbolKind.LBRACE);
        ArrayList<StatementNode> statements = parseStatements();
        match(SymbolKind.RBRACE);
        return new BlockNode(statements);
    }
    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}