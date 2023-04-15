package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class StatementListNode extends StatementNode {

    public StatementListNode() {
    }

    public static ArrayList<StatementNode> parseStatements() throws ParseException{
        ArrayList<StatementNode> statements = new ArrayList<>();
        if(lookahead.kind == SymbolKind.NUM){
            ArithmeticExpressionNode.parseArithmeticExpression();
        }
        if(lookahead.kind == SymbolKind.TRUE){
            match(SymbolKind.TRUE);
        }
        if(lookahead.kind == SymbolKind.FALSE){
            match(SymbolKind.FALSE);
        }
        if(lookahead.kind == SymbolKind.INT){
            match(SymbolKind.INT);
        }
        match(SymbolKind.SEMI);
        return statements;
    }
}
