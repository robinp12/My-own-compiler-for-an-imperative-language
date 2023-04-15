package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.match;

public class MethodNode extends ExpressionNode {
    //TODO

    private Symbol identifier;
    private TypeNode returnType;
    private ArrayList<ParamNode> parameters;
    private BlockNode body;

    public MethodNode(Symbol identifier, TypeNode returnType, ArrayList<ParamNode> parameters, BlockNode body) {
        this.identifier = identifier;
        this.returnType = returnType;
        this.parameters = parameters;
        this.body = body;
    }

    public static MethodNode parseMethod() throws ParseException{
        match(SymbolKind.PROC);
        Symbol name = match(SymbolKind.LITERAL);
        match(SymbolKind.LPAR);
        ArrayList<ParamNode> params = ParamListNode.parseParams();
        match(SymbolKind.RPAR);
        TypeNode returnType = TypeNode.parseType();
        BlockNode body = BlockNode.parseBlock();
        return new MethodNode(name, returnType, params, body);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
