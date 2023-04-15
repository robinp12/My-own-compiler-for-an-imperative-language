package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;

public class ConstantDeclarationNode extends ExpressionNode {
    private String name;
    private TypeNode type;
    private ValueNode initialValue;

    public ConstantDeclarationNode(String name, TypeNode type, ValueNode value){
        this.name = name;
        this.type = type;
        this.initialValue = value;
    }

    public static ExpressionNode parseDeclarationConst() throws ParseException {
        match(SymbolKind.CONST);
        Symbol identifier = match(SymbolKind.LITERAL);
        TypeNode type = TypeNode.parseType();
        match(SymbolKind.EQUALS);
        ValueNode value = ValueNode.parseValue();
        match(SymbolKind.SEMI);
        return new ConstantDeclarationNode(identifier.attribute, type, value);
    }
    @Override
    public String toString() {
        return "const " + name + " " + type + " = " + initialValue;
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
