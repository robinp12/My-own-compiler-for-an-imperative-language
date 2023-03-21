package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

public class ParamListNode extends ParamNode{

    public ParamListNode(String identifier, TypeNode type, String name) throws ParseException {
        super(identifier, type, name);
    }

    public ArrayList<ParamNode> parseParams() throws ParseException{
        ArrayList<ParamNode> parameters = new ArrayList<>();
        if(lookahead.kind != SymbolKind.RPAR){
            parameters.add(parseParam());
            while (lookahead.kind.equals(SymbolKind.COMA)){
                match(SymbolKind.COMA);
                parameters.add(parseParam());
            }
        }
        return parameters;
    }

}
