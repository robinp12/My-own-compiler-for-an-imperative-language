package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class VarDeclarationNode extends ExpressionNode {
    private String name;
    private TypeNode type;
    private ValueNode value;

    public VarDeclarationNode(String name, TypeNode type, ValueNode value){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public static ExpressionNode parseDeclarationVar() throws ParseException {
        match(SymbolKind.VAR);
        Symbol identifier = match(SymbolKind.LITERAL);
        TypeNode type = TypeNode.parseType();
        ValueNode value = null;
        if (lookahead.kind == SymbolKind.EQUALS){
            match(SymbolKind.EQUALS);
            value = ValueNode.parseValue();
        }
        match(SymbolKind.SEMI);
        return new VarDeclarationNode(identifier.attribute, type, value);
    }
    @Override
    public String toString() {
        return "var " + name + " " + type + " = " + value;
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
