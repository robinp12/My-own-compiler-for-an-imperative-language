package compiler.Parser.AST;

import compiler.Lexer.SymbolKind;
import compiler.Parser.Parser;

import java.text.ParseException;
import java.util.ArrayList;

import static compiler.Parser.Parser.lookahead;
import static compiler.Parser.Parser.match;

public class StatementNode extends ExpressionNode{
    private ArrayList<ExpressionNode> statements;
    public StatementNode(ArrayList<ExpressionNode> statements) {
        super("statement");
        this.statements = statements;
    }
    public ArrayList<ExpressionNode> getStatements() {
        return statements;
    }

    public static StatementNode parseStatement() throws ParseException {
        ArrayList<ExpressionNode> statements = new ArrayList<>();
        while (Parser.NotAtEnd() || lookahead != null){
            switch (lookahead.getKind()){
                case CONST:
                    statements.add(ConstantDeclarationNode.parseDeclarationConst());
                    break;
                case VAR:
                    statements.add(VarDeclarationNode.parseDeclarationVar());
                    break;
                case VAL:
                    statements.add(ValDeclarationNode.parseDeclarationVal());
                    break;
                case PROC:
                    statements.add(MethodNode.parseMethod());
                    break;
                case IF:
                    statements.add(IfStatementNode.parseIfStatement());
                    break;
                case WHILE:
                    statements.add(WhileStatementNode.parseWhileStatement());
                    break;
                case FOR:
                    statements.add(ForStatementNode.parseForStatement());
                    break;
                case RECORD:
                    statements.add(RecordNode.parseRecord());
                    break;
                case RETURN:
                    statements.add(ReturnNode.parseReturn());
                    break;
                case LITERAL:
                    LiteralNode literal = LiteralNode.parseLiteral();
                    if(lookahead.getKind().equals(SymbolKind.LPAR)){
                        statements.add(MethodCallNode.parseMethodCall(literal));
                    }else{
                        statements.add(AssignmentNode.parseAssignment(literal.getLiteral()));
                    }
                    break;
                case COMMENT:
                    match(SymbolKind.COMMENT);
                    break;
                case RBRACE:
                    return new StatementNode(statements);
                default:
                    throw new ParseException("Error during parsing: illegal symbol" + lookahead,-1);
            }
        }
        return new StatementNode(statements);
    }

    @Override
    public String toString() {
        return "StatementNode{" +
                "statements=" + statements +
                '}';
    }
}
