package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

public abstract class ExpressionNode {
    // Define common methods for all AST node classes here

    public ExpressionNode(){
    }

    public abstract <T> T accept(NodeVisitor visitor);
}
