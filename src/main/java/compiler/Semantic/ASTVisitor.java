package compiler.Semantic;

import compiler.Parser.AST.*;

public interface ASTVisitor {
    void visit(ProgramNode node);
    void visit(ArithmeticExpressionNode node);
    void visit(AssignmentArrayNode node);
    void visit(AssignmentNode node) throws Exception;
    void visit(BinaryExpressionNode node);
    void visit(BlockNode node);
    void visit(BooleanNode node);
    void visit(ConstantDeclarationNode node);
    void visit(ExpressionNode node);
    void visit(ForStatementNode node);
    void visit(IfStatementNode node);
    void visit(LiteralNode node);
    void visit(MethodCallNode node);
    void visit(MethodNode node);
    void visit(NumberNode node);
    void visit(ParamListNode node);
    void visit(ParamNode node);
    void visit(RecordNode node);
    void visit(RecordCallNode node);
    void visit(ReturnNode node);
    void visit(StatementNode node);
    void visit(StringNode node);
    void visit(ValDeclarationNode node);
    void visit(WhileStatementNode node);
    void visit(ValueNode node);
    void visit(VarDeclarationNode node);
    void visit(TypeNode node);

    // Add more visit methods for other node types
}