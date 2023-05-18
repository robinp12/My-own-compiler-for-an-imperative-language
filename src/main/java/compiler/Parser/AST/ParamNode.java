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
    public ParamNode(ValueNode value) {
        super("call_param");
        this.value = value;
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

    public static ParamNode parseCallParam() throws ParseException{
        if(lookahead.getKind().equals(SymbolKind.NUM)){
            String value = NumberNode.parseNumber().getValue();
            return new ParamNode(new ValueNode<>(value,"int"));
        }
        if(lookahead.getKind().equals(SymbolKind.LITERAL)){
            String value = LiteralNode.parseLiteral().getLiteral();
            return new ParamNode(new ValueNode<>(value,"literal"));
        }
        if(lookahead.getKind().equals(SymbolKind.STRING)){
            String value = StringNode.parseString().getValue();
            return new ParamNode(new ValueNode<>(value,"str"));
        }
        if(lookahead.getKind().equals(SymbolKind.TRUE) || lookahead.getKind().equals(SymbolKind.FALSE)){
            boolean value = BooleanNode.parseBoolean().isVal();
            return new ParamNode(new ValueNode<>(value,"bool"));
        }
        else {
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
