package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;

import java.text.ParseException;

import static compiler.Parser.Parser.match;
public class StringNode extends ExpressionNode {
    public final String value;

    public StringNode(String value){
        super();
        this.value = value;
    }

    public static StringNode parseStringExpression() throws ParseException {
        return new StringNode(match(SymbolKind.STRING).attribute);
    }

    @Override
    public <T> T accept(NodeVisitor visitor) {
        return null;
    }
}
