package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ParamNode extends ExpressionNode {
    private TypeNode type;
    private String identifier;

    public ParamNode(TypeNode type, String name) {
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
        Symbol identifier = match(SymbolKind.LITERAL);
        TypeNode type = TypeNode.parseType();
        return new ParamNode(type,identifier.getAttribute());
    }

    @Override
    public String toString() {
        return "ParamNode{" +
                "type=" + type +
                ", name='" + identifier + '\'' +
                '}';
    }
}
