package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class ValueNode<T> extends ExpressionNode {
    private final T value;

    public ValueNode(T value, String type) {
        super(type);
        this.value = value;
    }
    public T getValue() {
        return value;
    }



    @Override
    public String toString() {
        return "ValueNode{" +
                "value=" + value +
                '}';
    }
}
