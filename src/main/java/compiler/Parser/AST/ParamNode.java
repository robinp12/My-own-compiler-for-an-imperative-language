package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ParamNode extends ExpressionNode {
    private TypeNode type;
    private String identifier;

    public ParamNode(TypeNode type, String name) {
        super(type.getTypeStr());
        this.identifier = name;
        this.type = type;
    }
    public TypeNode getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static ParamNode parseParam() throws ParseException{
        String identifier = LiteralNode.parseLiteral().getLiteral();
        TypeNode type = TypeNode.parseType();
        return new ParamNode(type,identifier);
    }

    @Override
    public String toString() {
        return "ParamNode{" +
                "type=" + type +
                ", name='" + identifier + '\'' +
                '}';
    }
}
