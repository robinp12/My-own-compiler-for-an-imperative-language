package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ParamNode extends ExpressionNode {
    private TypeNode type;
    private String identifier;

    private ValueNode value;

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

    public static ParamNode parseParam() throws ParseException {
        String identifier = LiteralNode.parseLiteral().getLiteral();
        TypeNode type = TypeNode.parseType();
        return new ParamNode(type, identifier);
    }

    public static ParamNode parseCallParam() throws ParseException {
        SymbolKind next = lookahead.getKind();
        if (next.equals(SymbolKind.NUM)) {
            NumberNode value = NumberNode.parseNumber();
            return new ParamNode(new TypeNode(value.getTypeStr(), value.getValue()), value.getValue());
        }
        if (next.equals(SymbolKind.LITERAL)) {
            LiteralNode value = LiteralNode.parseLiteral();
            return new ParamNode(new TypeNode(value.getTypeStr(), value.getLiteral()), value.getLiteral());
        }
        if (next.equals(SymbolKind.STRING)) {
            StringNode value = StringNode.parseString();
            return new ParamNode(new TypeNode(value.getTypeStr(), value.getValue()), value.getValue());
        }
        if (next.equals(SymbolKind.TRUE) || next.equals(SymbolKind.FALSE)) {
            BooleanNode value = BooleanNode.parseBoolean();
            return new ParamNode(new TypeNode(value.getTypeStr(), next.getName()), next.getName());
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "ParamNode{" +
                "type=" + type +
                ", identifier='" + identifier + '\'' +
                ", value=" + value +
                '}';
    }
}
