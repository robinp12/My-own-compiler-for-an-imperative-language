package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import static compiler.Parser.Parser.match;

public class ParamNode extends ExpressionNode {
    private TypeNode type;
    private String name;

    public ParamNode(TypeNode type, String name) {
        this.name = name;
        this.type = type;
    }

    public static ParamNode parseParam() throws ParseException{
        TypeNode type = TypeNode.parseType();
        Symbol identifier = match(SymbolKind.LITERAL);
        return new ParamNode(type,identifier.attribute);
    }

}
