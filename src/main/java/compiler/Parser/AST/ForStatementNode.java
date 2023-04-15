package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class ForStatementNode extends StatementNode{

    ExpressionNode condition;
    ArrayList<StatementNode> thenStatements;
    ArrayList<StatementNode> elseStatements;
    public ForStatementNode(ExpressionNode condition, ArrayList<StatementNode> thenStatements, ArrayList<StatementNode> elseStatements){
        this.condition = condition;
        this.thenStatements = thenStatements;
        this.elseStatements = elseStatements;
    }

    public static ExpressionNode parseForStatement() throws ParseException {
        match(SymbolKind.FOR);
        //TODO
        return new ForStatementNode(null,null,null);
    }

}
