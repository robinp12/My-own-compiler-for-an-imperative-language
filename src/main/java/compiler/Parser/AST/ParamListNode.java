package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ParamListNode extends ExpressionNode {

    public static ArrayList<ParamNode> parseParams() throws ParseException{
        ArrayList<ParamNode> parameters = new ArrayList<>();
        if(lookahead.kind != SymbolKind.RPAR){
            parameters.add(ParamNode.parseParam());
            while (lookahead.kind.equals(SymbolKind.COMA)){
                match(SymbolKind.COMA);
                parameters.add(ParamNode.parseParam());
            }
        }
        return parameters;
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
