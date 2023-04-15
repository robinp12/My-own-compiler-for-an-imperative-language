package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;


public class ValDeclarationNode extends ExpressionNode{
    private String name;
    private TypeNode type;
    private ValueNode value;

    public ValDeclarationNode(String name, TypeNode type, ValueNode value){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public static ValDeclarationNode parseDeclarationVal() throws ParseException {
        match(SymbolKind.VAL);
        Symbol identifier = match(SymbolKind.LITERAL);
        TypeNode type = TypeNode.parseType();
        match(SymbolKind.EQUALS);
        ValueNode value = ValueNode.parseValue();
        match(SymbolKind.SEMI);
        return new ValDeclarationNode(identifier.attribute, type, value);
    }

    @Override
    public String toString() {
        return "val " + name + " " + type + " = " + value;
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
