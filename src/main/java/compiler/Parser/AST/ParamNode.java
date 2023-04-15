package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ParamNode extends ExpressionNode {
       private TypeNode type;
    private String name;

    public ParamNode(TypeNode type, String name) {
        this.name = name;
        this.type = type;
    }
    public TypeNode getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static ParamNode parseParam() throws ParseException{
        Symbol identifier = match(SymbolKind.LITERAL);
        TypeNode type = TypeNode.parseType();
        return new ParamNode(type,identifier.attribute);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
