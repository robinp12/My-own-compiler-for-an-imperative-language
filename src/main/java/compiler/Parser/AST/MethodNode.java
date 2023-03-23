package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class MethodNode {
    //TODO

    private String identifier;
    private TypeNode returnType;
    private ArrayList<ParamNode> parameters;
    private BlockNode body;

    public MethodNode(String identifier, TypeNode returnType, ArrayList<ParamNode> parameters, BlockNode body) throws ParseException {
        this.identifier = identifier;
        this.returnType = returnType;
        this.parameters = parameters;
        this.body = body;
    }

    public MethodNode parseMethod() throws ParseException{
        //TODO
        TypeNode returnType = null;
        String name = match(SymbolKind.LITERAL).attribute;
        match(SymbolKind.LPAR);
        ArrayList<ParamNode> params = null;
        match(SymbolKind.RPAR);
        BlockNode body = null;
        return new MethodNode(name,returnType,params,body);
    }

}
