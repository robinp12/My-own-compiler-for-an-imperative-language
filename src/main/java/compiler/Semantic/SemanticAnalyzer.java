package compiler.Semantic;

import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.*;
import compiler.Parser.Parser;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
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
        String varType = node.getType().getTypeSymbol();
        String valType = node.getValue().getTypeStr();

        if (valType.equals("binaryExp")){
            valType = visit((BinaryExpressionNode) node.getValue());
        }
        if (varType.equals("binaryExp")){
            varType = visit((BinaryExpressionNode) node.getValue());
        }
        //Check variable and value type
        if (!valType.equals(varType)) {
            throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType);
        }

        // Check if the variable has already been declared in this scope
        if (symbolTable.containmut(varName)) {
            if (!symbolTable.lookupmut(varName).getTypeSymbol().equals(varType)) {
                throw new Exception("Assignment error: this identifier " + varName + " is already used and is of type " + symbolTable.lookupmut(varName).getTypeSymbol() + " and not " + varType);
            }
        } else if (symbolTable.containimmut(varName)) {
            throw new Exception("Assignement exeption: you tried to modify a immuable val or const");
        } else if (symbolTable.containrecord(varName)){
            RecordCallNode rec = (RecordCallNode) node.getValue();
            if(!containsIdentifier(symbolTable.lookuprecord(varName), rec.getfield())){
                throw new Exception("Assignment error: bad field,"+ rec.getfield()+" not in record "+ varName);
            }else {
                visit((RecordCallNode) rec, varName);
            }
        }
        else {
            throw new Exception("Assignement exeption: illegal assignement");
        }
    }


    @Override
    public String visit(BinaryExpressionNode node) throws Exception {

        // Get the types of the left and right operands
        String leftType = node.getLeft().getTypeStr();
        String rightType = node.getRight().getTypeStr();

        if (leftType.equals("binaryExp")){
            leftType = visit((BinaryExpressionNode) node.getLeft());
        }
        if (rightType.equals("binaryExp")){
            rightType = visit((BinaryExpressionNode) node.getLeft());
        }
        if (leftType.equals("str")){
            LiteralNode left = (LiteralNode) node.getLeft();
            if (symbolTable.containmut(left.getLiteral())){
                leftType = symbolTable.lookupmut(left.getLiteral()).getTypeStr();
            } else if (symbolTable.containimmut(left.getLiteral())) {
                leftType = symbolTable.lookupimmut(left.getLiteral()).getTypeStr();
            }
        }
        if (rightType.equals("str")){
            LiteralNode right = (LiteralNode) node.getRight();
            if (symbolTable.containmut(right.getLiteral())){
                rightType = symbolTable.lookupmut(right.getLiteral()).getTypeStr();
            } else if (symbolTable.containimmut(right.getLiteral())) {
                rightType = symbolTable.lookupimmut(right.getLiteral()).getTypeStr();
            }
        }

        // Determine the result type based on the operator
        SymbolKind operatorKind = node.getOperator().getKind();
        switch (operatorKind) {
            case PLUS:
                // Concatenation for strings
                if (leftType.equals("str") && rightType.equals("str")) {
                    node.setResultType("str");
                }
                // Addition for numeric types
                else if ((leftType.equals("int") || leftType.equals("real")) && (rightType.equals("int") || rightType.equals("real"))) {
                    // Promote int to real if necessary
                    if (leftType.equals("int") && rightType.equals("real")) {
                        node.setResultType("real");
                    } else if (leftType.equals("real") && rightType.equals("int")) {
                        node.setResultType("real");
                    } else {
                        // Same type, either int or real
                        node.setResultType(leftType);
                    }
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;
            case MINUS:
            case STAR:
            case SLASH:
            case PERC:
                // Arithmetic operations for numeric types
                if ((leftType.equals("int") || leftType.equals("real")) && (rightType.equals("int") || rightType.equals("real"))) {
                    // Promote int to real if necessary
                    if (leftType.equals("int") && rightType.equals("real")) {
                        node.setResultType("real");
                    } else if (leftType.equals("real") && rightType.equals("int")) {
                        node.setResultType("real");
                    } else {
                        // Same type, either int or real
                        node.setResultType(leftType);
                    }
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;
            case EQEQ:
            case DIFF:
                // Comparison operations for compatible types
                if ((leftType.equals("int") && rightType.equals("real")) || (leftType.equals("real") && rightType.equals("int")) || leftType.equals(rightType)) {
                    node.setResultType("bool");
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;
            case LESS:
            case LESSEQ:
            case MORE:
            case MOREEQ:
                // Comparison operations for numeric types
                if ((leftType.equals("int") || leftType.equals("real")) && (rightType.equals("int") || rightType.equals("real"))) {
                    node.setResultType("bool");
                } else {
                    throw new Exception("Invalid operation: binary expression error type: left type = "+ leftType+ " and right type = "+rightType);
                }
                break;
            case AND:
            case OR:
                // Logical operations for boolean types
                if (leftType.equals("bool") && rightType.equals("bool")) {
                    node.setResultType(leftType);
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;
            default:
                throw new Exception("Invalid operator: binary expression operator error");
        }
        return node.getResultType();
    }

    @Override
    public void visit(BlockNode node) throws Exception {
        System.out.println("block");
        visit(node.getStatements());
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
            if(node.getAssignment().getValue()==null){
                throw new Exception("Semantic error: No value assigned to const");
            }
            String valType = node.getAssignment().getValue().getTypeStr();
            if (valType.equals("binaryExp")){
                valType = visit((BinaryExpressionNode) node.getAssignment().getValue());
            }
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
            if (symbolTable.containmut(node.getVariable()))
                if (node.getStart() instanceof NumberNode && node.getEnd() instanceof NumberNode && node.getStep() instanceof NumberNode){
                    visit(node.getBlock());
                } else {
                    throw new Exception("Illegal type of start, stop or step");
                }
            else
                throw new Exception("Error in the for loop, you must declare var before loop");
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
            if(node.getElseStatements()!=null){
                visit(node.getElseStatements());
            }
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
    public void visit(RecordNode node) throws Exception {
        if (node.getTypeStr().equals("record")) {
            // Check if the variable has already been declared in this scope
            if (symbolTable.containmut(node.getIdentifier()) || symbolTable.containimmut(node.getIdentifier()) || symbolTable.containrecord(node.getIdentifier())) {
                throw new Exception("Assignment error: this identifier " + node.getIdentifier() + " is already used ");
            } else {
                List<SimpleEntry<String, String>> record_entries = new ArrayList<>();

                for (ParamNode pn : node.getFields()) {
                    if (containsIdentifier(record_entries, pn.getIdentifier())) {
                        throw new Exception("Illegal record declaration: duplicate identifier");
                    } else {
                        visit(pn);
                        record_entries.add(new SimpleEntry<>(pn.getIdentifier(), pn.getTypeStr()));
                    }
                }
                symbolTable.insertrecord(node.getIdentifier(), record_entries);
            }
            System.out.println("record");
        }
    }

    @Override
    public void visit(RecordCallNode node) {

    }


    private boolean containsIdentifier(List<SimpleEntry<String, String>> list, String identifier) {
        for (SimpleEntry<String, String> entry : list) {
            if (entry.getKey().equals(identifier)) {
                return true;
            }
        }
        return false;
    }

    private String getTypeId(List<SimpleEntry<String, String>> list, String identifier) throws Exception {
        for (SimpleEntry<String, String> entry : list) {
            if (entry.getKey().equals(identifier)) {
                return entry.getValue();
            }
        }
        throw new Exception("Record Error");
    }


    public void visit(RecordCallNode node, String identifier) throws Exception {
        System.out.println(node);
        String valtype = node.getvalue().getTypeStr();
        String rectype = getTypeId(symbolTable.lookuprecord(identifier), node.getfield());
        if(!rectype.equals(valtype)){
            throw new Exception("Record error: Bad type, value type "+valtype+ " does not match record type:"+rectype);
        }
    }

    @Override
    public void visit(ReturnNode node) {
        System.out.println("return");
    }

    @Override
    public void visit(StatementNode node) throws Exception {
        System.out.println("statements");
        for (ExpressionNode statement : node.getStatements()) {
            if (statement.getTypeStr() == "str") {
                visit((ValDeclarationNode) statement);
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
            if (valType.equals("binaryExp")){
                valType = visit((BinaryExpressionNode) node.getAssignment().getValue());
            }
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
    public void visit(WhileStatementNode node) throws Exception {
        System.out.println("while");
        visit((BinaryExpressionNode) node.getCondition());
        visit(node.getBlock());

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
            if (node.getAssignment().getValue() != null) {
                String valType = node.getAssignment().getValue().getTypeStr();
                if (valType.equals("binaryExp")){
                    valType = visit((BinaryExpressionNode) node.getAssignment().getValue());
                }
                if (!valType.equals(varType.getTypeSymbol())) {
                    throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
                }
            }
            //Check variable and value type
            symbolTable.insertmut(varName, varType);
        } else {
            throw new Exception("Semantic error: duplicated var declaration");
        }
    }

    @Override
    public void visit(TypeNode node) throws Exception {
        List<String> validTypes = Arrays.asList("int", "str", "real", "bool", "record");
        if (validTypes.contains(node.getTypeSymbol())) {
            System.out.println("Valid type");
        } else {
            throw new Exception("Illegal type encountered");
        }
    }

    public void visit(List<ExpressionNode> a) throws Exception {
        for (ExpressionNode expressionNode : a) {
            visit(expressionNode);
        }
    }
}
