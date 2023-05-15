package compiler.Semantic;

import compiler.Parser.AST.*;
import compiler.Parser.Parser;

import java.util.List;

public class SemanticAnalyzer implements ASTVisitor {
    private static Parser parser;

    private static SymbolTable symbolTable;


    public SemanticAnalyzer(ProgramNode programNode) throws Exception {
        if (programNode.getTypeStr() == "root") {
            symbolTable = new SymbolTable();
            visit(programNode);
        } else {
            throw new Exception("Semantic error ProgramNode");
        }
    }

    @Override
    public void visit(ProgramNode node) throws Exception {
        System.out.println("program");
        for (ExpressionNode expression : node.getExpressions()) {
            //System.out.println(expression);
            visit(expression);
        }
    }

    @Override
    public void visit(ArithmeticExpressionNode node) {
        System.out.println("arithmetic expression");

    }

    @Override
    public void visit(AssignmentArrayNode node) {
        System.out.println("assignment array");
    }

    @Override
    public void visit(AssignmentNode node) throws Exception {
        System.out.println("assignment");
        String varName = node.getIdentifier();
        TypeNode varType = node.getType();
        String valType = node.getValue().getTypeStr();

        //Check variable and value type
        if (!valType.equals(varType.getTypeSymbol())) {
            throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
        }

        // Check if the variable has already been declared in this scope
        if (symbolTable.containmut(varName)) {
            if (!symbolTable.lookupmut(varName).getTypeSymbol().equals(varType.getTypeSymbol())) {
                throw new Exception("Assignment error: this identifier " + varName + " is already used and is of type " + symbolTable.lookupmut(varName).getTypeSymbol() + " and not " + varType.getTypeSymbol());
            }
        } else if (symbolTable.containimmut(varName)) {
            throw new Exception("Assignement exeption: you tried to modify a immuable val or const");
        } else {
            throw new Exception("Assignement exeption: illegal assignement");
        }
    }


    @Override
    public void visit(BinaryExpressionNode node) {
        System.out.println("binary expression");
        //TODO
    }

    @Override
    public void visit(BlockNode node) throws Exception {
        System.out.println("block");
    }

    @Override
    public void visit(BooleanNode node) {
        System.out.println("boolean");
    }

    @Override
    public void visit(ConstantDeclarationNode node) throws Exception {
        System.out.println("const");
        if (!symbolTable.containimmut(node.getAssignment().getIdentifier())) {
            String varName = node.getAssignment().getIdentifier();
            TypeNode varType = node.getAssignment().getType();
            String valType = node.getAssignment().getValue().getTypeStr();

            //Check variable and value type
            if (!valType.equals(varType.getTypeSymbol())) {
                throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
            }
            symbolTable.insertimmut(varName, varType);
        } else {
            throw new Exception("Semantic error: duplicated const declaration");
        }
    }

    @Override
    public void visit(ExpressionNode node) throws Exception {
        System.out.println("expression");
        if (node instanceof VarDeclarationNode) {
            visit((VarDeclarationNode) node);
        } else if (node instanceof ValDeclarationNode) {
            visit((ValDeclarationNode) node);
        } else if (node instanceof ConstantDeclarationNode) {
            visit((ConstantDeclarationNode) node);
        } else if (node instanceof AssignmentNode) {
            visit((AssignmentNode) node);
        } else if (node instanceof AssignmentArrayNode) {
            visit((AssignmentArrayNode) node);
        } else if (node instanceof BinaryExpressionNode) {
            visit((BinaryExpressionNode) node);
        } else if (node instanceof BlockNode) {
            visit((BlockNode) node);
        } else if (node instanceof BooleanNode) {
            visit((BooleanNode) node);
        } else if (node instanceof ForStatementNode) {
            visit((ForStatementNode) node);
        } else if (node instanceof IfStatementNode) {
            visit((IfStatementNode) node);
        } else if (node instanceof LiteralNode) {
            visit((LiteralNode) node);
        } else if (node instanceof MethodCallNode) {
            visit((MethodCallNode) node);
        } else if (node instanceof MethodNode) {
            visit((MethodNode) node);
        } else if (node instanceof NumberNode) {
            visit((NumberNode) node);
        } else if (node instanceof ParamListNode) {
            visit((ParamListNode) node);
        } else if (node instanceof ParamNode) {
            visit((ParamNode) node);
        } else if (node instanceof ProgramNode) {
            visit((ProgramNode) node);
        } else if (node instanceof RecordCallNode) {
            visit((RecordCallNode) node);
        } else if (node instanceof RecordNode) {
            visit((RecordNode) node);
        } else if (node instanceof ReturnNode) {
            visit((ReturnNode) node);
        } else if (node instanceof StatementNode) {
            visit((StatementNode) node);
        } else if (node instanceof StringNode) {
            visit((StringNode) node);
        } else if (node instanceof TypeNode) {
            visit((TypeNode) node);
        } else if (node instanceof ValueNode<?>) {
            visit((ValueNode) node);
        } else if (node instanceof WhileStatementNode) {
            visit((WhileStatementNode) node);
        }

    }

    @Override
    public void visit(ForStatementNode node) throws Exception {
        System.out.println("for");
        if (node.getTypeStr() == "ForLoop") {
            visit(node.getBlock());
        } else {
            throw new Exception();
        }
        ;
    }

    @Override
    public void visit(IfStatementNode node) throws Exception {
        System.out.println("If");
        if (node.getTypeStr() == "If") {
            visit(node.getCondition());
            visit(node.getThenStatements());
            visit(node.getElseStatements());
        } else {
            throw new Exception();
        }
        ;

    }

    @Override
    public void visit(LiteralNode node) {
        System.out.println("literal");
    }

    @Override
    public void visit(MethodCallNode node) {

    }

    @Override
    public void visit(MethodNode node) {
        System.out.println("method");
    }

    @Override
    public void visit(NumberNode node) {
        System.out.println("number");
    }

    @Override
    public void visit(ParamListNode node) {
        System.out.println("param list");
    }

    @Override
    public void visit(ParamNode node) {
        System.out.println("param");
    }

    @Override
    public void visit(RecordNode node) {
        System.out.println("record");
    }

    @Override
    public void visit(RecordCallNode node) {

    }

    @Override
    public void visit(ReturnNode node) {
        System.out.println("return");
    }

    @Override
    public void visit(StatementNode node) throws Exception {
        System.out.println("statements");
        for (ExpressionNode statement : node.getStatements()) {
            System.out.println(statement.getTypeStr());
            if (statement.getTypeStr() == "str") {
                visit((ValDeclarationNode) statement);
            }
            if(statement.getTypeStr().equals("bool")){
                visit((ReturnNode) statement);
            }
            //visit((ForStatementNode) statement);
        }
    }

    @Override
    public void visit(StringNode node) {
        System.out.println("string");
    }

    @Override
    public void visit(ValDeclarationNode node) throws Exception {
        System.out.println("val");
        if (!symbolTable.containimmut(node.getAssignment().getIdentifier())) {
            String varName = node.getAssignment().getIdentifier();
            TypeNode varType = node.getAssignment().getType();
            String valType = node.getAssignment().getValue().getTypeStr();

            //Check variable and value type
            if (!valType.equals(varType.getTypeSymbol())) {
                throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
            }
            symbolTable.insertimmut(varName, varType);
        } else {
            throw new Exception("Semantic error: duplicated val declaration");
        }
    }

    @Override
    public void visit(WhileStatementNode node) {
        System.out.println("while");

    }

    @Override
    public void visit(ValueNode node) {
        System.out.println("value");
    }

    @Override
    public void visit(VarDeclarationNode node) throws Exception {
        System.out.println("var");
        if (!symbolTable.containmut(node.getAssignment().getIdentifier())) {
            String varName = node.getAssignment().getIdentifier();
            TypeNode varType = node.getAssignment().getType();
            String valType = node.getAssignment().getValue().getTypeStr();

            //Check variable and value type
            if (!valType.equals(varType.getTypeSymbol())) {
                throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
            }
            symbolTable.insertmut(varName, varType);
        } else {
            throw new Exception("Semantic error: duplicated var declaration");
        }
    }

    @Override
    public void visit(TypeNode node) {
        System.out.println("type");
    }

    public void visit(List<ExpressionNode> a) throws Exception {
        for (ExpressionNode expressionNode : a) {
            visit(expressionNode);
        }
    }
}
