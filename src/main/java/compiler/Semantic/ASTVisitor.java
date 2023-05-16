package compiler.Semantic;

import compiler.Parser.AST.*;

public interface ASTVisitor {
    void visit(ProgramNode node) throws Exception;
    void visit(ArithmeticExpressionNode node);
    void visit(AssignmentArrayNode node);
    void visit(AssignmentNode node) throws Exception;
    String visit(BinaryExpressionNode node) throws Exception;
    void visit(BlockNode node) throws Exception;
    void visit(BooleanNode node);
    void visit(ConstantDeclarationNode node) throws Exception;
    void visit(ExpressionNode node) throws Exception;
    void visit(ForStatementNode node) throws Exception;
    void visit(IfStatementNode node) throws Exception;
    void visit(LiteralNode node);
    void visit(MethodCallNode node);
    void visit(MethodNode node);
    void visit(NumberNode node);
    void visit(ParamListNode node);
    void visit(ParamNode node);
    void visit(RecordNode node) throws Exception;
    void visit(RecordCallNode node);
    void visit(ReturnNode node);
    void visit(StatementNode node) throws Exception;
    void visit(StringNode node);
    void visit(ValDeclarationNode node) throws Exception;
    void visit(WhileStatementNode node);
    void visit(ValueNode node);
    void visit(VarDeclarationNode node) throws Exception;
    void visit(TypeNode node) throws Exception;

    // Add more visit methods for other node types
}