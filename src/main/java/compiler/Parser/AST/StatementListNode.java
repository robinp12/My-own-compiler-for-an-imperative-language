package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class StatementListNode extends StatementNode {

    public StatementListNode(ExpressionNode expression) {
        super(expression);
    }

    public static ArrayList<StatementNode> parseStatements() throws ParseException{
        ArrayList<StatementNode> statements = new ArrayList<>();
        match(SymbolKind.RETURN);
        if(lookahead.kind == SymbolKind.NUM){
            ArithmeticExpressionNode.parseArithmeticExpression();
        }
        if(lookahead.kind == SymbolKind.TRUE){
            match(SymbolKind.TRUE);
        }
        if(lookahead.kind == SymbolKind.FALSE){
            match(SymbolKind.FALSE);
        }
        match(SymbolKind.SEMI);
        return statements;
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
