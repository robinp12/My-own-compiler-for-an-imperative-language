package compiler.Parser.AST;

public interface NodeVisitor {
    <T> T visit(ProgramNode node);
    <T> T visit(ParamNode node);
    <T> T visit(AssignmentNode node);
    <T> T visit(ArithmeticExpressionNode node);
    <T> T visit(BinaryExpressionNode node);
    <T> T visit(BlockNode node);
    <T> T visit(MethodNode node);
    <T> T visit(NumberExpressionNode node);
    <T> T visit(ParamListNode node);
    <T> T visit(RecordCallNode node);
    <T> T visit(RecordNode node);
    <T> T visit(TypeNode node);
    // Add more visit methods for other node types as needed
}
