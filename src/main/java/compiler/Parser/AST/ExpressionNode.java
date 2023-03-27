package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;

public abstract class ExpressionNode {
    // Define common methods for all AST node classes here
    public abstract <T> T accept(NodeVisitor<T> visitor);

}
