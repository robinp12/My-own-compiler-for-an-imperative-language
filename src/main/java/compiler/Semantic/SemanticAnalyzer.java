package compiler.Semantic;

import com.google.common.collect.Lists;
import compiler.Compiler;
import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.*;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class SemanticAnalyzer implements ASTVisitor {
    private static SymbolTable symbolTable;

    public static List<String> functions = Lists.newArrayList(
            "not",
            "chr",
            "len",
            "floor",
            "readint",
            "readreal",
            "readstring",
            "writeint",
            "writereal",
            "write",
            "writeln"
    );
    private final Map<String, ArrayList<ParamNode>> functionTable;

    public SemanticAnalyzer(ProgramNode programNode) throws Exception {
        this.functionTable = new HashMap<>();
        if (programNode.getTypeStr().equals("root")) {
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
    public String visit(AssignmentArrayNode node) {
        return null;
    }

    @Override
    public void visit(AssignmentNode node) throws Exception {
        System.out.println("assignment");
        String varName = node.getIdentifier();
        String varType = node.getType().getTypeSymbol();
        String valType = node.getValue().getTypeStr();

        if (valType.equals("binaryExp")) {
            valType = visit((BinaryExpressionNode) node.getValue());
        }
        if (varType.equals("binaryExp")) {
            varType = visit((BinaryExpressionNode) node.getValue());
        }
        if (valType.equals("array")) {
            visit((AssignmentArrayNode) node.getValue(), varName);
            return;
        }

        //Check variable and value type
        if (!valType.equals(varType)) {
            throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType);
        }

        // Check if the variable has already been declared in this scope
        if (SymbolTable.containmut(varName)) {
            if (!symbolTable.lookupmut(varName).getTypeSymbol().equals(varType)) {
                throw new Exception("Assignment error: this identifier " + varName + " is already used and is of type " + symbolTable.lookupmut(varName).getTypeSymbol() + " and not " + varType);
            }
        } else if (SymbolTable.containimmut(varName)) {
            throw new Exception("Assignment exception: you tried to modify a immutable val or const");
        } else if (SymbolTable.containrecord(varName)) {
            RecordCallNode rec = (RecordCallNode) node.getValue();
            if (!containsIdentifier(SymbolTable.lookuprecord(varName), rec.getfield())) {
                throw new Exception("Assignment error: bad field," + rec.getfield() + " not in record " + varName);
            } else {
                visit(rec, varName);
            }
        } else {
            throw new Exception("Assignment exception: Illegal assignment");
        }
    }

    private void visit(AssignmentArrayNode value, String varName) throws Exception {
        //Check variable and value type

        // Check if the variable has already been declared in this scope
        if (SymbolTable.containarray(varName)) {
            String varType = SymbolTable.lookuparray(varName).getKey();
            Integer size = SymbolTable.lookuparray(varName).getValue();
            if (!varType.equals(value.getType().getTypeSymbol()) || Integer.parseInt(((NumberNode)value.getIndex()).getValue())>=size) {
                throw new Exception("Illegal array assignement");
            }
        } else {
            throw new Exception("Assignment exception: Illegal assignment, assignation to not  declareted array");
        }


    }


    @Override
    public String visit(BinaryExpressionNode node) throws Exception {

        // Get the types of the left and right operands
        String leftType = node.getLeft().getTypeStr();
        String rightType = node.getRight().getTypeStr();

        if (leftType.equals("binaryExp")) {
            leftType = visit((BinaryExpressionNode) node.getLeft());
        }
        if (rightType.equals("binaryExp")) {
            rightType = visit((BinaryExpressionNode) node.getRight());
        }
        if (leftType.equals("str")) {
            LiteralNode left = (LiteralNode) node.getLeft();
            if (SymbolTable.containmut(left.getLiteral())) {
                leftType = SymbolTable.lookupmut(left.getLiteral()).getTypeStr();
            } else if (SymbolTable.containimmut(left.getLiteral())) {
                leftType = SymbolTable.lookupimmut(left.getLiteral()).getTypeStr();
            }
        }
        if (rightType.equals("str")) {
            LiteralNode right = (LiteralNode) node.getRight();
            if (SymbolTable.containmut(right.getLiteral())) {
                rightType = SymbolTable.lookupmut(right.getLiteral()).getTypeStr();
            } else if (SymbolTable.containimmut(right.getLiteral())) {
                rightType = SymbolTable.lookupimmut(right.getLiteral()).getTypeStr();
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
                    throw new Exception("Invalid operation: binary expression error type: left type = " + leftType + " and right type = " + rightType);
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
        // Get the types of the left and right operands
        ExpressionNode left = node.getLeft();
        ExpressionNode right = node.getRight();

        if (left.getTypeStr().equals("binaryExp")) {
            visit((BinaryExpressionNode) node.getLeft());
        }
        if (right.getTypeStr().equals("binaryExp")) {
            visit((BinaryExpressionNode) node.getRight());
        }

        switch (node.getResultType()) {
            case "str":
                String lstr = null;
                String rstr = null;
                if (left instanceof BinaryExpressionNode){
                    lstr = ((BinaryExpressionNode) left).getResult();
                }else if(left instanceof LiteralNode){
                    //TODO in code generation, involve variable
                } else {
                    lstr = ((LiteralNode)left).getLiteral();
                }
                if (right instanceof BinaryExpressionNode){
                    rstr = ((BinaryExpressionNode) right).getResult();
                }else if(left instanceof LiteralNode){
                    //TODO in code generation, involve variable
                } else {
                    rstr = ((LiteralNode) left).getLiteral();
                }

                if (operatorKind.equals(SymbolKind.PLUS)) {
                    node.setResult(lstr + rstr);
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;

            case "int":
                Integer lint = null;
                Integer rint = null;
                if (left instanceof BinaryExpressionNode){
                    lint = Integer.valueOf(((BinaryExpressionNode) left).getResult());
                } else if(left instanceof LiteralNode){
                    //TODO in code generation, involve variable
                }
                else {
                    lint = Integer.valueOf(((NumberNode) left).getValue());
                }
                if (right instanceof BinaryExpressionNode){
                    rint = Integer.valueOf(((BinaryExpressionNode) right).getResult());
                } else if(left instanceof LiteralNode){
                    //TODO in code generation, involve variable
                } else {
                    rint = Integer.valueOf(((NumberNode) right).getValue());
                }

                if (operatorKind.equals(SymbolKind.PLUS)) {
                    node.setResult(String.valueOf(lint+ rint));
                } else if (operatorKind.equals(SymbolKind.MINUS)) {
                    node.setResult(String.valueOf(lint-rint));
                } else if (operatorKind.equals(SymbolKind.STAR)) {
                    node.setResult(String.valueOf(lint*rint));
                } else if (operatorKind.equals(SymbolKind.SLASH)) {
                    node.setResult(String.valueOf(lint/rint));
                } else if (operatorKind.equals(SymbolKind.PERC)) {
                    node.setResult(String.valueOf(lint%rint));
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;


            case "real":
                Float lreal = null;
                Float rreal = null;
                if (left instanceof BinaryExpressionNode){
                    lreal = Float.valueOf(((BinaryExpressionNode) left).getResult());
                }else if(left instanceof LiteralNode){
                    //TODO in code generation, involve variable
                } else {
                    lreal = Float.valueOf(((NumberNode) left).getValue());
                }
                if (right instanceof BinaryExpressionNode){
                    rreal = Float.valueOf(((BinaryExpressionNode) right).getResult());
                }else if(left instanceof LiteralNode){
                    //TODO in code generation, involve variable
                } else {
                    rreal = Float.valueOf(((NumberNode) right).getValue());
                }

                if (operatorKind.equals(SymbolKind.PLUS)) {
                    node.setResult(String.valueOf(lreal + rreal));
                } else if (operatorKind.equals(SymbolKind.MINUS)) {
                    node.setResult(String.valueOf(lreal - rreal));
                } else if (operatorKind.equals(SymbolKind.STAR)) {
                    node.setResult(String.valueOf(lreal * rreal));
                } else if (operatorKind.equals(SymbolKind.SLASH)) {
                    node.setResult(String.valueOf(lreal / rreal));
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;

            case "bool":
                if (left.getTypeStr().equals("bool")) {
                    Boolean lb = null;
                    Boolean rb = null;
                    if (left instanceof BinaryExpressionNode) {
                        lb = Boolean.valueOf(((BinaryExpressionNode) left).getResult());
                    } else {
                        lb = Boolean.valueOf(((NumberNode) left).getValue());
                    }
                    if (right instanceof BinaryExpressionNode) {
                        rb = Boolean.valueOf(((BinaryExpressionNode) right).getResult());
                    } else {
                        rb = Boolean.valueOf(((NumberNode) right).getValue());
                    }
                    if (operatorKind.equals(SymbolKind.EQEQ)) {
                        node.setResult(String.valueOf(lb == rb));
                    } else if (operatorKind.equals(SymbolKind.DIFF)) {
                        node.setResult(String.valueOf(lb != rb));
                    } else {
                        throw new Exception("Invalid operation: binary expression error type");
                    }
                    //TODO: AND et OR
                    break;
                } else if (left.getTypeStr().equals("str")) {
                    String ls = null;
                    String rs = null;
                    if (left instanceof BinaryExpressionNode){
                        ls = ((BinaryExpressionNode) left).getResult();
                    } else {
                        ls = ((LiteralNode)left).getLiteral();
                    }
                    if (right instanceof BinaryExpressionNode){
                        rs = ((BinaryExpressionNode) right).getResult();
                    } else {
                        rs = ((LiteralNode) left).getLiteral();
                    }
                    if (operatorKind.equals(SymbolKind.EQEQ)) {
                        node.setResult(String.valueOf(ls.equals(rs)));
                    } else if (operatorKind.equals(SymbolKind.DIFF)) {
                        node.setResult(String.valueOf(!ls.equals(rs)));
                    } else {
                        throw new Exception("Invalid operation: binary expression error type");
                    }
                } else {
                    Float lr = null;
                    Float rr = null;
                    if (left instanceof BinaryExpressionNode){
                        lr = Float.valueOf(((BinaryExpressionNode) left).getResult());
                    } else {
                        lr = Float.valueOf(((NumberNode) left).getValue());
                    }
                    if (right instanceof BinaryExpressionNode){
                        rr = Float.valueOf(((BinaryExpressionNode) right).getResult());
                    } else {
                        rr = Float.valueOf(((NumberNode) right).getValue());
                    }
                    System.out.println(lr);
                    System.out.println(rr);

                    if (operatorKind.equals(SymbolKind.EQEQ)) {
                        node.setResult(String.valueOf(lr.equals(rr)));
                    } else if (operatorKind.equals(SymbolKind.DIFF)) {
                        node.setResult(String.valueOf(!lr.equals(rr)));
                    } else if (operatorKind.equals(SymbolKind.LESS)) {
                        node.setResult(String.valueOf(lr < rr));
                    } else if (operatorKind.equals(SymbolKind.MORE)) {
                        node.setResult(String.valueOf(lr > rr));
                    } else if (operatorKind.equals(SymbolKind.LESSEQ)) {
                        node.setResult(String.valueOf(lr <= rr));
                    } else if (operatorKind.equals(SymbolKind.MOREEQ)) {
                        node.setResult(String.valueOf(lr >= rr));
                    } else {
                        throw new Exception("Invalid operation: binary expression error type");
                    }
                    break;
                }
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
        if (!SymbolTable.containimmut(node.getAssignment().getIdentifier())) {
            String varName = node.getAssignment().getIdentifier();
            TypeNode varType = node.getAssignment().getType();
            if (node.getAssignment().getValue() == null) {
                throw new Exception("Semantic error: No value assigned to const");
            }
            String valType = node.getAssignment().getValue().getTypeStr();
            if (valType.equals("binaryExp")) {
                valType = visit((BinaryExpressionNode) node.getAssignment().getValue());
            }
            //Check variable and value type
            if (!valType.equals(varType.getTypeSymbol())) {
                throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
            }
            SymbolTable.insertimmut(varName, varType);
        } else {
            throw new Exception("Semantic error: duplicated const declaration");
        }
    }

    @Override
    public void visit(ExpressionNode node) throws Exception {
        System.out.println(node);
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
        if (node.getTypeStr().equals("ForLoop")) {
            if (SymbolTable.containmut(node.getVariable()))
                if (node.getStart() instanceof NumberNode && node.getEnd() instanceof NumberNode && node.getStep() instanceof NumberNode) {
                    visit(node.getBlock());
                } else {
                    throw new Exception("Illegal type of start, stop or step");
                }
            else
                throw new Exception("Error in the for loop, you must declare var before loop");
        } else {
            throw new Exception();
        }
    }

    @Override
    public void visit(IfStatementNode node) throws Exception {
        System.out.println("If");
        if (node.getTypeStr().equals("If")) {
            visit(node.getCondition());
            visit(node.getThenStatements());
            if (node.getElseStatements() != null) {
                visit(node.getElseStatements());
            }
        } else {
            throw new Exception();
        }

    }

    @Override
    public void visit(LiteralNode node) {
        System.out.println("literal");
    }

    @Override
    public void visit(MethodCallNode node) throws Exception {
        switch (node.getIdentifier()) {
            case "readint", "readreal","readstring" -> {
                if (node.getParameters().size() != 0) {
                    throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need no argument");
                }
                if (Compiler.argu==null || Compiler.argu.length!=1) {
                    throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need 1 argument in LINE COMMAND");
                }
            }
            case "write", "writeln" -> {
                if (node.getParameters().size() != 1) {
                    throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need 1 argument");
                }
            }
            case "writeint", "writereal", "not", "chr", "len", "floor" -> {
                if (node.getParameters().size() != 1) {
                    throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need exactly 1 argument");
                }
            }
        }
        if(node.getParameters().size()>0) {
            ParamNode param = node.getParameters().get(0);
            switch (node.getIdentifier()) {
                case "writeint", "writereal" -> {
                    if (node.getParameters().size() != 1) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need exactly 1 argument");
                    }
                    if (!param.getTypeStr().equals("str")) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" parameter type need to be \"str\"");
                    }
                }
                case "not" -> {
                    if (node.getParameters().size() != 1) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need exactly 1 argument");
                    }
                    if (!param.getTypeStr().equals("bool")) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" parameter type need to be \"bool\"");
                    }
                }
                case "chr" -> {
                    if (node.getParameters().size() != 1) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need exactly 1 argument");
                    }
                    if (!param.getTypeStr().equals("int")) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" parameter type need to be \"int\"");
                    }
                }
                case "len" -> {
                    if (node.getParameters().size() != 1) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need exactly 1 argument");
                    }
                    if (!param.getTypeStr().equals("str") && !param.getTypeStr().equals("array")) { //TODO
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" parameter type need to be \"str\" or \"array\"");
                    }
                }
                case "floor" -> {
                    if (node.getParameters().size() != 1) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" need exactly 1 argument");
                    }
                    if (!param.getTypeStr().equals("real")) {
                        throw new Exception("Built-in function \"" + node.getIdentifier() + "\" parameter type need to be \"real\"");
                    }
                }
            }
        }
        String identifier = node.getIdentifier();
        ArrayList<ParamNode> function = functionTable.get(identifier);
        if(function==null && !functions.contains(identifier)){
            try {
                throw new Exception("Function \"" + identifier + "\" is not implemented yet");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (functionTable.containsKey(identifier)) {
            if (function.size() != node.getParameters().size()) {
                try {
                    throw new Exception("Function \"" + identifier + "\" need " + function.size() + " argument(s)");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            for (int i = 0; i < function.size(); i++) {
                if(!function.get(i).getTypeStr().equals(node.getParameters().get(i).getTypeStr())){
                    try {
                        throw new Exception("In function \"" + identifier +"\", argument \""+ function.get(i).getIdentifier() +"\" need to be \"" + function.get(i).getTypeStr()+"\" type");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public void visit(MethodNode node) throws Exception {
        System.out.println("method");
        String name = node.getIdentifier();
        if (functions.contains(name)) {
            throw new Exception("Reserved keyword : \"" + node.getIdentifier() + "\" is used for built-in function");
        }
        if(functionTable.containsKey(name)){
            throw new Exception("Function named \"" + node.getIdentifier() + "\" is already used");

        }
        functionTable.put(name, node.getParameters());
        if(node.getBody().getStatements()!=null){
            visit(node.getBody());
        }

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
            if (SymbolTable.containmut(node.getIdentifier()) || SymbolTable.containimmut(node.getIdentifier()) || SymbolTable.containrecord(node.getIdentifier())) {
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
                SymbolTable.insertrecord(node.getIdentifier(), record_entries);
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
        String rectype = getTypeId(SymbolTable.lookuprecord(identifier), node.getfield());
        if (!rectype.equals(valtype)) {
            throw new Exception("Record error: Bad type, value type " + valtype + " does not match record type:" + rectype);
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
            visit(statement);
        }
    }

    @Override
    public void visit(StringNode node) {
        System.out.println("string");
    }

    @Override
    public void visit(ValDeclarationNode node) throws Exception {
        System.out.println("val");
        if (!SymbolTable.containmut(node.getAssignment().getIdentifier()) && !SymbolTable.containimmut(node.getAssignment().getIdentifier()) && !SymbolTable.containrecord(node.getAssignment().getIdentifier()) && !SymbolTable.containarray(node.getAssignment().getIdentifier())) {
            String varName = node.getAssignment().getIdentifier();
            TypeNode varType = node.getAssignment().getType();
            String valType = node.getAssignment().getValue().getTypeStr();
            if (valType.equals("binaryExp")) {
                valType = visit((BinaryExpressionNode) node.getAssignment().getValue());
            }
            //Check variable and value type
            if (!valType.equals(varType.getTypeSymbol())) {
                throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
            }
            SymbolTable.insertimmut(varName, varType);
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
        if (!SymbolTable.containmut(node.getAssignment().getIdentifier()) && !SymbolTable.containimmut(node.getAssignment().getIdentifier()) && !SymbolTable.containrecord(node.getAssignment().getIdentifier()) && !SymbolTable.containarray(node.getAssignment().getIdentifier())) {
            String varName = node.getAssignment().getIdentifier();
            TypeNode varType = node.getAssignment().getType();
            if(node.getAssignment().getValue() instanceof MethodCallNode){
                MethodCallNode val = (MethodCallNode) node.getAssignment().getValue();
                System.out.println(val.getIdentifier());
                if(functions.contains(val.getIdentifier())){

                }else{
                    throw new Exception("Assignment error, " + val.getIdentifier() + " does not match");
                }
            }
            else if (node.getAssignment().getValue() instanceof AssignmentArrayNode ){
                AssignmentArrayNode val = (AssignmentArrayNode) node.getAssignment().getValue();
                if(varType.getTypeSymbol().equals(val.getType().getTypeSymbol())){
                    SymbolTable.insertarray(varName,new SimpleEntry<>(val.getType().getTypeSymbol(),Integer.valueOf(val.getSize().getValue())));
                    return;
                }else{
                    throw new Exception("Different type in array definition" );
                }
            }
            else if (node.getAssignment().getValue() != null) {
                String valType = node.getAssignment().getValue().getTypeStr();
                if (valType.equals("binaryExp")) {
                    valType = visit((BinaryExpressionNode) node.getAssignment().getValue());
                }
                if (valType.equals("array")){
                    valType = visit((AssignmentArrayNode) node.getAssignment().getValue());
                }
                else if (!valType.equals(varType.getTypeSymbol())) {
                    throw new Exception("Assignment error, value type " + valType + " does not match declared type " + varType.getTypeSymbol());
                }
            }
            //Check variable and value type
            SymbolTable.insertmut(varName, varType);
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
