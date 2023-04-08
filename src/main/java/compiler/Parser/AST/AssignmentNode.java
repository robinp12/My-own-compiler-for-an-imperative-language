package compiler.Parser.AST;

import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;


public class AssignmentNode extends StatementNode{
    // TODO

    private Symbol identifier;
    private TypeNode type;
    private Symbol value;

    public AssignmentNode(ExpressionNode expression, Symbol identifier, TypeNode type, Symbol value) {
        super(expression);
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public static AssignmentNode parseAssignment() throws ParseException{
        ExpressionNode expressionNode = new ExpressionNode() {
            @Override
            public <T> T accept(NodeVisitor visitor) {
                return null;
            }
        };
        //VariableDeclarationNode left = VariableDeclarationNode.parseDeclarationVar();
        Symbol identifier = match(SymbolKind.LITERAL);
        TypeNode type = TypeNode.parseType();
        match(SymbolKind.EQUALS);
        Symbol value = null;
        switch (lookahead.kind){
            case NUM -> value = match(SymbolKind.NUM);
            case TRUE -> value = match(SymbolKind.TRUE);
            case FALSE -> value = match(SymbolKind.FALSE);
        }
        return new AssignmentNode(expressionNode,identifier, type, value);
    }
}
