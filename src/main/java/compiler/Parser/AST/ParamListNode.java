package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ParamListNode extends ExpressionNode {

    public ParamListNode(String type) {
        super(type);
    }

    public static ArrayList<ParamNode> parseParams(boolean isMethodCall) throws ParseException{
        ArrayList<ParamNode> parameters = new ArrayList<>();
        if(lookahead.getKind() != SymbolKind.RPAR){
            if(isMethodCall){
                parameters.add(ParamNode.parseCallParam());
            }else {
                parameters.add(ParamNode.parseParam());
            }
            while (lookahead.getKind().equals(SymbolKind.COMA)){
                match(SymbolKind.COMA);
                if(isMethodCall){
                    parameters.add(ParamNode.parseCallParam());
                }else {
                    parameters.add(ParamNode.parseParam());
                }
            }
        }
        return parameters;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
