package compiler.Semantic;

import compiler.Parser.AST.*;
import compiler.Parser.Parser;

public class SemanticAnalyzer implements ASTVisitor{
    private static Parser parser;

    @Override
    public void visit(ProgramNode node) {

    }

    @Override
    public void visit(ArithmeticExpressionNode node) {

    }

    @Override
    public void visit(AssignmentArrayNode node) {

    }

    @Override
    public void visit(AssignmentNode node) throws Exception {
        // Get the name of the variable being assigned to
        String varName = node.getIdentifier();
        String varType = node.getType().getTypeSymbol();
        String valType = node.getValue().getTypeStr();

        // Check if the variable has already been declared in this scope
        if (!SymbolTable.contains(varName)) {
            if (!node.getValue().equals(null))
                throw new Exception("Variable " + varName + " has not been declared.");

            SymbolTable.insert(varName,null);
        }

        // Get the type of the variable being assigned to

        // Check that the expression being assigned has a compatible type
        //TypeNode exprType = node.getExpression().getType();
       // if (!varType.isAssignableFrom(exprType)) {
         //   throw new SemanticException("Cannot assign expression of type " + exprType + " to variable " + varName + " of type " + varType);
       // }

        // Set the type of the assignment node to be the same as the type of the expression
       // node.setType(exprType);

    }

    @Override
    public void visit(BinaryExpressionNode node) {

    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(BooleanNode node) {

    }

    @Override
    public void visit(ConstantDeclarationNode node) {

    }

    @Override
    public void visit(ExpressionNode node) {

    }

    @Override
    public void visit(ForStatementNode node) {

    }

    @Override
    public void visit(IfStatementNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override
    public void visit(MethodCallNode node) {

    }

    @Override
    public void visit(MethodNode node) {

    }

    @Override
    public void visit(NumberNode node) {

    }

    @Override
    public void visit(ParamListNode node) {

    }

    @Override
    public void visit(ParamNode node) {

    }

    @Override
    public void visit(RecordNode node) {

    }

    @Override
    public void visit(RecordCallNode node) {

    }

    @Override
    public void visit(ReturnNode node) {

    }

    @Override
    public void visit(StatementNode node) {

    }

    @Override
    public void visit(StringNode node) {

    }

    @Override
    public void visit(ValDeclarationNode node) {

    }

    @Override
    public void visit(WhileStatementNode node) {

    }

    @Override
    public void visit(ValueNode node) {

    }

    @Override
    public void visit(VarDeclarationNode node) {

    }

    @Override
    public void visit(TypeNode node) {

    }
}
