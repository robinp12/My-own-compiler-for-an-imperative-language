package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

public class ParamNode extends TypeNode{
    private TypeNode type;
    private String name;

    public ParamNode(String identifier, TypeNode type, String name) throws ParseException {
        super(identifier);
        this.name = name;
        this.type = type;
    }

    public ParamNode parseParam() throws ParseException{
        TypeNode type = parseType();
        Symbol identifier = match(SymbolKind.LITERAL);
        return new ParamNode(identifier.string,type,name);
    }

}
