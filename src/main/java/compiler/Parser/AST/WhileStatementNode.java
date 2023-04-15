package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class WhileStatementNode extends StatementNode{

    ExpressionNode condition;
    ArrayList<StatementNode> statements;
    public WhileStatementNode(ExpressionNode condition, ArrayList<StatementNode> statements){
        this.condition = condition;
        this.statements = statements;
    }

    public static ExpressionNode parseWhileStatement() throws ParseException {
        match(SymbolKind.WHILE);
        ExpressionNode condition = BinaryExpressionNode.parseConditionNode();
        match(SymbolKind.LBRACE);
        ArrayList<StatementNode> stmts = StatementListNode.parseStatements();
        match(SymbolKind.RBRACE);

        return new WhileStatementNode(condition, stmts);
    }
    @Override
    public String toString() {
        return "while (" + condition + ") " + statements;
    }
}
