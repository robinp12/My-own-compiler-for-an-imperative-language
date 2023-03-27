public interface NodeVisitor {
    T visit(ProgramNode node);
    T visit(ParamNode node);
    T visit(AssignmentNode node);
    T visit(ArithmeticExpressionNode node);
    T visit(BinaryExpressionNode node);
    T visit(BlockNode node);
    T visit(MethodNode node);
    T visit(NumberExpressionNode node);
    T visit(ParamListNode node);
    T visit(RecordCallNode node);
    T visit(RecordNode node);
    T visit(TypeNode node);
    // Add more visit methods for other node types as needed
}
