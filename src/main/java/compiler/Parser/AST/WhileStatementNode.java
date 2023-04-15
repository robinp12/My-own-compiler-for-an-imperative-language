package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class WhileStatementNode extends StatementNode{

    ExpressionNode condition;
    ArrayList<StatementNode> thenStatements;
    ArrayList<StatementNode> elseStatements;
    public WhileStatementNode(ExpressionNode condition, ArrayList<StatementNode> thenStatements, ArrayList<StatementNode> elseStatements){
        super(condition);
        this.condition = condition;
        this.thenStatements = thenStatements;
        this.elseStatements = elseStatements;
    }

    public static ExpressionNode parseWhileStatement() throws ParseException {
        match(SymbolKind.WHILE);
        //TODO CONDITIONAL
        match(SymbolKind.LITERAL);
        if(lookahead.kind==SymbolKind.LESSEQ) match(SymbolKind.LESSEQ);
        if(lookahead.kind==SymbolKind.MOREEQ) match(SymbolKind.MOREEQ);
        if(lookahead.kind==SymbolKind.NOTEQ) match(SymbolKind.NOTEQ);
        if(lookahead.kind==SymbolKind.EQEQ) match(SymbolKind.EQEQ);
        match(SymbolKind.NUM);
        match(SymbolKind.LBRACE);
        //TODO BLOCK
        match(SymbolKind.RBRACE);
        return new WhileStatementNode(null,null,null);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
