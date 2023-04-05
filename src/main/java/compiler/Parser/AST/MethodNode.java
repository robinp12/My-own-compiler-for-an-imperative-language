package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class MethodNode extends ExpressionNode {
    //TODO

    private String identifier;
    private TypeNode returnType;
    private ArrayList<ParamNode> parameters;
    private BlockNode body;

    public MethodNode(String identifier, TypeNode returnType, ArrayList<ParamNode> parameters, BlockNode body) {
        this.identifier = identifier;
        this.returnType = returnType;
        this.parameters = parameters;
        this.body = body;
    }

    public static MethodNode parseMethod() throws ParseException{
        match(SymbolKind.PROC);
        String name = match(SymbolKind.LITERAL).attribute;
        match(SymbolKind.LPAR);
        ArrayList<ParamNode> params = ParamListNode.parseParams();
        match(SymbolKind.RPAR);
        TypeNode returnType = TypeNode.parseType();
        BlockNode body = BlockNode.parseBlock();
        System.out.println(name);
        return new MethodNode(name, returnType, params, body);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
