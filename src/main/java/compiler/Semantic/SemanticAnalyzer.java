package compiler.Semantic;

import compiler.Parser.AST.*;
import compiler.Parser.Parser;

import java.util.List;

public class SemanticAnalyzer implements ASTVisitor{
    private static Parser parser;
    private static SymbolTable symbolTable;


    public SemanticAnalyzer(ProgramNode programNode) throws Exception {
        System.out.println("Program Node");
        if(programNode.getTypeStr() == "root"){
            symbolTable = new SymbolTable();
            visit(programNode);
        }
        else{
            throw new Exception("Semantic error ProgramNode");
        }
    }

    @Override
    public void visit(ProgramNode node) throws Exception {
        for (ExpressionNode expression : node.getExpressions()) {
            visit(expression);
        }
    }

    @Override
    public void visit(ArithmeticExpressionNode node) {

    }

    @Override
    public void visit(AssignmentArrayNode node) {

    }

    @Override
    public void visit(AssignmentNode node) throws Exception {
        System.out.println("Assignment Node");
        String varName = node.getIdentifier();
        TypeNode varType = node.getType();
        String valType = node.getValue().getTypeStr();

        //Check variable and value type
        if(!valType.equals(varType.getTypeSymbol())){
            throw new Exception("Assignment error, value type " + valType+" does not match declared type "+varType.getTypeSymbol());
        }

        // Check if the variable has already been declared in this scope
        if (symbolTable.contains(varName)) {
            throw new Exception("Assignment error: this identifier is already used" + varName);
        }
        else {
            symbolTable.insert(varName, varType);
        }
    }


    @Override
    public void visit(BinaryExpressionNode node) {

    }

    @Override
    public void visit(BlockNode node) throws Exception {
        System.out.println("block");
        visit(node.getStatements());
    }

    @Override
    public void visit(BooleanNode node) {}

    @Override
    public void visit(ConstantDeclarationNode node) throws Exception {
        if (!SymbolTable.contains(node.getAssignment().getIdentifier())){
            visit(node.getAssignment());
        }
        else{
            throw new Exception("Semantic error: duplicated constant declaration");
        }
    }

    @Override
    public void visit(ExpressionNode node) throws Exception {
        System.out.println("expression");
        visit((ValDeclarationNode) node);
    }

    @Override
    public void visit(ForStatementNode node) throws Exception {
        System.out.println("for");
        if(node.getTypeStr()=="ForLoop"){
            visit(node.getBlock());
        }
        else{
            throw new Exception();
        };
    }

    @Override
    public void visit(IfStatementNode node) throws Exception {
        System.out.println("If");
        if(node.getTypeStr()=="If"){
            visit(node.getCondition());
            visit(node.getThenStatements());
            visit(node.getElseStatements());
        }
        else{
            throw new Exception();
        };

    }

    @Override
    public void visit(LiteralNode node) {}

    @Override
    public void visit(MethodCallNode node) {

    }

    @Override
    public void visit(MethodNode node) {

    }

    @Override
    public void visit(NumberNode node) {}

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
    public void visit(StatementNode node) throws Exception {
        System.out.println("statements");
        for (ExpressionNode statement : node.getStatements()) {
            if(statement.getTypeStr()=="str"){
                visit((ValDeclarationNode) statement);
            }
            //visit((ForStatementNode) statement);
        }
    }

    @Override
    public void visit(StringNode node) {

    }

    @Override
    public void visit(ValDeclarationNode node) throws Exception {
        System.out.println("val");
        if (!SymbolTable.contains(node.getAssignment().getIdentifier())){
            visit(node.getAssignment());
        }
        else{
            throw new Exception("Semantic error: duplicated constant declaration");
        }
    }

    @Override
    public void visit(WhileStatementNode node) {

    }

    @Override
    public void visit(ValueNode node) {

    }

    @Override
    public void visit(VarDeclarationNode node) throws Exception {
        System.out.println("var");
        visit(node.getAssignment());
    }

    @Override
    public void visit(TypeNode node) {}

    public void visit(List<ExpressionNode> a) throws Exception {
        for (ExpressionNode expressionNode : a) {
            visit(expressionNode);
        }
    }
}
