package compiler.Parser.AST;

import compiler.Parser.Parser;

import java.text.ParseException;
import java.util.ArrayList;

public class StatementNode extends ExpressionNode{
    public ArrayList<ExpressionNode> statements;
    public StatementNode(ArrayList<ExpressionNode> statements) {
        this.statements = statements;
    }

    public static StatementNode parseStatement() throws ParseException {
        ArrayList<ExpressionNode> statements = new ArrayList<>();
        while (Parser.NotAtEnd()){
            switch (Parser.lookahead.kind){
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
                default:
                    throw new ParseException("Error during parsing: illegal symbol" + Parser.lookahead,-1);
            }
        }
        return new StatementNode(statements);
    }
}
