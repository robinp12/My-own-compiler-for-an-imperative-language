package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

public class TypeNode extends ExpressionNode {
    private final Symbol typeSymbol;

    public TypeNode(Symbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public Symbol getTypeSymbol() {
        return typeSymbol;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}