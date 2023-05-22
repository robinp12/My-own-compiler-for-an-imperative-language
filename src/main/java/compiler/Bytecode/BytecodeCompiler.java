package compiler.Bytecode;

import compiler.Lexer.Symbol;
import compiler.Parser.AST.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

import javax.naming.PartialResultException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static compiler.Bytecode.AsmUtils.invokeStatic;
import static compiler.Lexer.SymbolKind.LESS;
import static compiler.Lexer.SymbolKind.LESSEQ;
import static org.objectweb.asm.Opcodes.*;


public class BytecodeCompiler {
    private final ClassWriter container;
    private ClassWriter recordClass;
    /* MethodVisitor for current method. */
    private MethodVisitor methodMain;
    private MethodVisitor method;
    private int idx = 1;
    private final Map<String, Integer> valueTable;
    private StringBuilder argLetter;
    private String returnTypeLetter;

    public BytecodeCompiler(ProgramNode ast) {
        System.out.println("------ BYTECODE ------");
        this.valueTable = new HashMap<>();
        this.recordClass = null;

        // Class
        container = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        container.visit(V1_8, ACC_PUBLIC, "Main", null, "java/lang/Object", null);
        // Constructor
        MethodVisitor methodInit = container.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodInit.visitCode();
        methodInit.visitVarInsn(ALOAD, 0);
        methodInit.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodInit.visitInsn(RETURN);
        methodInit.visitMaxs(-1, -1);
        methodInit.visitEnd();
        // Others methods
        root(ast);
        container.visitEnd();
        System.out.println("Variable Table : " + valueTable);
    }

    public void getRender() {
        try (FileOutputStream fos = new FileOutputStream("Output.class")) {
            fos.write(getGeneration());
            System.out.println("Le bytecode a ete enregistre dans Output.class.\n\n");
            getState();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'enregistrement du bytecode : " + e.getMessage());
        }
    }

    public void getState() {
        ClassReader cr = new ClassReader(container.toByteArray());
        CheckClassAdapter.verify(cr, true, new PrintWriter(System.out));
        System.out.println("Verification terminee.");
    }

    public byte[] getGeneration() {
        return container.toByteArray();
    }

    public void root(ProgramNode node) {
        // Method main
        methodMain = container.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        methodMain.visitCode();

        if (node.getTypeStr().equals("root")) {
            for (ExpressionNode expression : node.getExpressions()) {
                generateExpression(expression);
            }
        }

        methodMain.visitInsn(RETURN);
        methodMain.visitMaxs(-1, -1);
        methodMain.visitEnd();
    }

    public void generateExpression(ExpressionNode expression) {
        System.out.println(expression);
        if (expression instanceof VarDeclarationNode) {
            generateVar(((VarDeclarationNode) expression).getAssignment(), (method == null) ? methodMain : method);
        } else if (expression instanceof ValDeclarationNode) {
            generateVar(((ValDeclarationNode) expression).getAssignment(), (method == null) ? methodMain : method);
        } else if (expression instanceof ConstantDeclarationNode) {
            generateConst((ConstantDeclarationNode) expression);
        } else if (expression instanceof RecordNode) {
            generateRecord((RecordNode) expression);
        } else if (expression instanceof StatementNode) {
            generateStatement((StatementNode) expression);
        } else if (expression instanceof MethodNode) {
            generateProc((MethodNode) expression);
        } else if (expression instanceof BinaryExpressionNode) {
            generateBinaryExpression((BinaryExpressionNode) expression);
        } else if (expression instanceof ForStatementNode) {
            generateFor((ForStatementNode) expression, (method == null) ? methodMain : method);
        } else if (expression instanceof IfStatementNode) {
            generateIf((IfStatementNode) expression);
        } else if (expression instanceof AssignmentNode) {
            generateAssignment((AssignmentNode) expression, (method == null) ? methodMain : method);
        } else if (expression instanceof WhileStatementNode) {
            generateWhile((WhileStatementNode) expression);
        } else if (expression instanceof ReturnNode) {
            generateReturn((ReturnNode) expression);
        } else if (expression instanceof MethodCallNode) {
            generateProcCall((MethodCallNode) expression);
        }
        /*else if (expression instanceof AssignmentArrayNode) {
            visit((AssignmentArrayNode) expression);
        } else if (expression instanceof BooleanNode) {
            visit((BooleanNode) expression);
        } else if (expression instanceof LiteralNode) {
            visit((LiteralNode) expression);
        }  else if (expression instanceof NumberNode) {
            visit((NumberNode) expression);
        } else if (expression instanceof ParamListNode) {
            visit((ParamListNode) expression);
        } else if (expression instanceof ParamNode) {
            visit((ParamNode) expression);
        } else if (expression instanceof ProgramNode) {
            visit((ProgramNode) expression);
        } else if (expression instanceof RecordCallNode) {
            visit((RecordCallNode) expression);
        }  else if (expression instanceof StringNode) {
            visit((StringNode) expression);
        } else if (expression instanceof TypeNode) {
            visit((TypeNode) expression);
        } else if (expression instanceof ValueNode<?>) {
            visit((ValueNode) expression);
        } */

    }

    private void generateProcCall(MethodCallNode expression) {
        System.out.println("methodCallNode");
        String name = expression.getIdentifier();
        for (ParamNode parameter : expression.getParameters()) {
            switch (parameter.getType().getTypeStr()) {
                case "int" -> {
                    methodMain.visitLdcInsn(Integer.parseInt(parameter.getIdentifier()));
                }
                case "str" -> {
                    methodMain.visitLdcInsn(parameter.getIdentifier());
                }
                case "bool" -> {
                    methodMain.visitInsn(parameter.getIdentifier().equals("true") ? ICONST_1 : ICONST_0);
                }
                case "real" -> {
                    methodMain.visitLdcInsn(Float.parseFloat(parameter.getIdentifier()));
                }
            }
        }
        // Appeler la methode dans main
        methodMain.visitMethodInsn(INVOKESTATIC, "Main", name, "(" + argLetter + ")" + returnTypeLetter, false);
        methodMain.visitInsn(POP);
    }

    private void generateWhile(WhileStatementNode expression) {
        System.out.println("while");
        BinaryExpressionNode exp = (BinaryExpressionNode) expression.getCondition();

        Label loopStart = new Label();
        Label loopEnd = new Label();

        // Loop while
        method.visitLabel(loopStart);

        // Condition left >= right
        generateBinaryExpression(exp);
        method.visitJumpInsn(IFEQ, loopEnd);

        generateBlock(expression.getBlock());

        method.visitJumpInsn(GOTO, loopStart); // Revenir au début de la boucle
        method.visitLabel(loopEnd);
    }

    private void generateIf(IfStatementNode expression) {
        method = method == null ? methodMain : method;

        //BooleanNode valBool = (BooleanNode) expression.getCondition();
        System.out.println(expression.getCondition());
        boolean hasElse = expression.getElseStatements() != null;
        Label elseLabel = new Label();
        Label endLabel = new Label();

        if (expression.getCondition() instanceof BinaryExpressionNode) {
            BinaryExpressionNode stmt = (BinaryExpressionNode) expression.getCondition();
            generateBinaryExpression(stmt);
        }
        if (expression.getCondition() instanceof BooleanNode) {
            int isTrue = ((BooleanNode) expression.getCondition()).isVal() ? ICONST_1 : ICONST_0;
            method.visitInsn(isTrue);
        }
        method.visitJumpInsn(IFEQ, hasElse ? elseLabel : endLabel);

        generateBlock(expression.getThenStatements());
        if (hasElse) {
            method.visitJumpInsn(GOTO, endLabel);
            method.visitLabel(elseLabel);
            generateBlock(expression.getElseStatements());
        }
        method.visitLabel(endLabel);

    }

    private void generateFor(ForStatementNode expression, MethodVisitor method) {
        NumberNode start = (NumberNode) expression.getStart();
        NumberNode step = (NumberNode) expression.getStep();
        NumberNode end = (NumberNode) expression.getEnd();
        int valStart = Integer.parseInt(start.getValue());
        int valStep = Integer.parseInt(step.getValue());
        int valEnd = Integer.parseInt(end.getValue());

        // Declare and initialize the variable 'i' with the value
        method.visitLdcInsn(valStart);
        method.visitVarInsn(ISTORE, 1);

        // Label for the beginning of the loop
        Label loopStart = new Label();
        method.visitLabel(loopStart);
        method.visitFrame(F_APPEND, 1, new Object[]{INTEGER}, 0, null);

        // Code inside the loop
        generateBlock(expression.getBlock());

        // Increment 'i'
        method.visitVarInsn(ILOAD, 1);
        method.visitLdcInsn(valStep);
        method.visitInsn(IADD);
        method.visitVarInsn(ISTORE, 1);

        // Condition for continuing the loop
        method.visitVarInsn(ILOAD, 1);
        method.visitLdcInsn(valEnd);
        Label loopEnd = new Label();
        method.visitJumpInsn(IF_ICMPLE, loopStart);

        method.visitLabel(loopEnd);
        method.visitFrame(F_CHOP, 1, null, 0, null);
    }

    private void generateBlock(BlockNode block) {
        System.out.println("Block");
        StatementNode stmt = block.getStatements();
        generateStatement(stmt);
    }

    private void generateStatement(StatementNode expression) {
        for (ExpressionNode statement : expression.getStatements()) {
            System.out.println("generate statement");
            generateExpression(statement);
        }
    }

    private void generateAssignment(AssignmentNode statement, MethodVisitor method) {
        System.out.println("array assignment : " + statement);
        if (valueTable.containsKey(statement.getIdentifier())) {
            int register = valueTable.get(statement.getIdentifier());
            System.out.println("var in table at register :" + register);
            System.out.println(statement.getType().getTypeStr());
            switch (statement.getType().getTypeStr()) {
                case "real" -> {
                    if(statement.getValue() instanceof AssignmentArrayNode){
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        NumberNode value = (NumberNode) array.getValue();
                        NumberNode index = (NumberNode) array.getIndex();

                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        method.visitLdcInsn(Float.parseFloat(value.getValue()));
                        method.visitInsn(FASTORE);
                    }else {
                        NumberNode value = (NumberNode) statement.getValue();
                        method.visitLdcInsn(Float.parseFloat(value.getValue()));
                        method.visitVarInsn(FSTORE, register);
                    }
                }
                case "int" -> {
                    if(statement.getValue() instanceof AssignmentArrayNode){
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        NumberNode value = (NumberNode) array.getValue();
                        NumberNode index = (NumberNode) array.getIndex();

                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        method.visitLdcInsn(Integer.parseInt(value.getValue()));
                        method.visitInsn(IASTORE);
                    }else{
                        NumberNode value = (NumberNode) statement.getValue();
                        method.visitLdcInsn(Integer.parseInt(value.getValue()));
                        method.visitVarInsn(ISTORE, register);
                    }
                }
                case "bool" -> {
                    if(statement.getValue() instanceof AssignmentArrayNode){
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        BooleanNode value = (BooleanNode) array.getValue();
                        NumberNode index = (NumberNode) array.getIndex();

                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        int isTrue = value.isVal() ? ICONST_1 : ICONST_0;
                        method.visitInsn(isTrue);
                        method.visitInsn(IASTORE);
                    }else {
                        BooleanNode value = (BooleanNode) statement.getValue();
                        int isTrue = value.isVal() ? ICONST_1 : ICONST_0;
                        method.visitInsn(isTrue);
                        method.visitVarInsn(ISTORE, register);
                    }
                }
                case "str" -> {
                    if(statement.getValue() instanceof AssignmentArrayNode){
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        System.out.println(array);
                        NumberNode index = (NumberNode) array.getIndex();
                        StringNode value = (StringNode) array.getValue();

                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        method.visitLdcInsn(value.getValue());
                        method.visitInsn(IASTORE);
                    }else {
                        StringNode value = (StringNode) statement.getValue();
                        method.visitLdcInsn(value.getValue());
                        method.visitVarInsn(ASTORE, register);
                    }
                }
                case "binaryExp" -> {
                    BinaryExpressionNode node = (BinaryExpressionNode) statement.getValue();
                    comparison(node.getOperator(), IFEQ, IF_ICMPEQ, IF_ACMPEQ, node.getLeft(), node.getRight());
                }
            }
        }
    }

    private void generateReturn(ReturnNode statement) {
        ExpressionNode val = statement.getValue();
        if(val instanceof NumberNode){
            NumberNode value = (NumberNode) statement.getValue();
            method.visitLdcInsn(Integer.parseInt(value.getValue()));
            method.visitInsn(IRETURN);
        }
        else if (val instanceof LiteralNode) {
            System.out.println("return");
            String leftId = ((LiteralNode) val).getLiteral();
            System.out.println(leftId);
            method.visitVarInsn(ILOAD, 2);


        } else if (val.getTypeStr().equals("str")) {
            StringNode value = (StringNode) statement.getValue();
            method.visitLdcInsn(value.getValue());
            method.visitInsn(ARETURN);
        } else if (val.getTypeStr().equals("binaryExp")) {
            //generateBinaryExpression((BinaryExpressionNode) statement.getValue());
            ExpressionNode left = ((BinaryExpressionNode) val).getLeft();
            ExpressionNode right = ((BinaryExpressionNode) val).getRight();
            System.out.println(left);
            System.out.println(right);
            String leftId = ((LiteralNode) left).getLiteral();
            String rightId = ((LiteralNode) right).getLiteral();
            method.visitVarInsn(ILOAD, 1);
            method.visitVarInsn(ILOAD, 2);
            method.visitInsn(IADD);
        }

    }

    public void generateConst(ConstantDeclarationNode expression) {
        AssignmentNode assignment = expression.getAssignment();
        String type = assignment.getType().getTypeStr();
        switch (type) {
            case "str" -> {
                StringNode val = (StringNode) assignment.getValue();
                container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "Ljava/lang/String;", null, val.getValue()).visitEnd();
            }
            case "int" -> {
                NumberNode val = (NumberNode) assignment.getValue();
                container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "I", null, Integer.parseInt(val.getValue())).visitEnd();
            }
            case "bool" -> {
                BooleanNode val = (BooleanNode) assignment.getValue();
                container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "Z", null, val.isVal()).visitEnd();
            }
            case "real" -> {
                NumberNode val = (NumberNode) assignment.getValue();
                container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "F", null, Float.parseFloat(val.getValue())).visitEnd();
            }
        }
    }

    public void generateVar(AssignmentNode expression, MethodVisitor method) {
        System.out.println("var || val");
        String type = expression.getTypeStr();
        if (type.equals("int")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof BinaryExpressionNode) {
                BinaryExpressionNode exp = (BinaryExpressionNode) valType;
                System.out.println(exp);
                generateBinaryExpression(exp);
            } else if (valType instanceof AssignmentArrayNode) {
                // Case of array
                AssignmentArrayNode val = (AssignmentArrayNode) expression.getValue();
                NumberNode size = (NumberNode) val.getSize();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitIntInsn(BIPUSH, Integer.parseInt(size.getValue()));
                method.visitIntInsn(NEWARRAY, T_INT);
                method.visitVarInsn(ASTORE, idx);
            } else {
                System.out.println(expression);
                NumberNode val = (NumberNode) expression.getValue();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitLdcInsn(Integer.parseInt((val != null) ? val.getValue() : "0"));
                method.visitVarInsn(ISTORE, idx);
            }
        }
        if (type.equals("str")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof AssignmentArrayNode) {
                // Case of array
                AssignmentArrayNode val = (AssignmentArrayNode) expression.getValue();
                NumberNode size = (NumberNode) val.getSize();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitIntInsn(BIPUSH, Integer.parseInt(size.getValue()));
                method.visitTypeInsn(ANEWARRAY, "Ljava/lang/String;");
                method.visitVarInsn(ASTORE, idx);
            } else {
                StringNode val = (StringNode) expression.getValue();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitLdcInsn((val != null) ? val.getValue() : "");
                method.visitVarInsn(ASTORE, idx);
            }
        }
        if (type.equals("bool")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof AssignmentArrayNode) {
                // Case of array
                AssignmentArrayNode val = (AssignmentArrayNode) expression.getValue();
                NumberNode size = (NumberNode) val.getSize();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitIntInsn(BIPUSH, Integer.parseInt(size.getValue()));
                method.visitIntInsn(NEWARRAY, T_BOOLEAN);
                method.visitVarInsn(ASTORE, idx);
            } else {
                BooleanNode val = (BooleanNode) expression.getValue();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                if (val != null) {
                    int isTrue = val.isVal() ? ICONST_1 : ICONST_0;
                    method.visitInsn(isTrue);
                } else {
                    method.visitInsn(ICONST_0);
                }
                method.visitVarInsn(ISTORE, idx);
            }
        }
        if (type.equals("real")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof AssignmentArrayNode) {
                // Case of array
                AssignmentArrayNode val = (AssignmentArrayNode) expression.getValue();
                NumberNode size = (NumberNode) val.getSize();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitIntInsn(BIPUSH, Integer.parseInt(size.getValue()));
                method.visitIntInsn(NEWARRAY, T_FLOAT);
                method.visitVarInsn(ASTORE, idx);
            } else {
                NumberNode val = (NumberNode) expression.getValue();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitLdcInsn(Float.parseFloat((val != null) ? val.getValue() : "0"));
                method.visitVarInsn(FSTORE, idx);

            }
        }
    }

    public void generateRecord(RecordNode record) {
        System.out.println("pas encore : " + record);
        String id = record.getIdentifier();
        String name = id.substring(0, 1).toUpperCase() + id.substring(1);
    }

    public void generateProc(MethodNode expression) {
        String name = expression.getIdentifier();
        //TODO Create table for args linked to function
        returnTypeLetter = switch (expression.getReturnType().getTypeSymbol()) {
            case "str" -> "Ljava/lang/String;";
            case "int" -> "I";
            case "bool" -> "Z";
            case "reel" -> "F";
            default -> "V";
        };
        // Return type

        // Arguments type
        argLetter = new StringBuilder();
        for (ParamNode parameter : expression.getParameters()) {
            // Add variable in the scope
            valueTable.put(parameter.getIdentifier(), ++idx);

            switch (parameter.getTypeStr()) {
                case "str" -> argLetter.append("Ljava/lang/String;");
                case "int" -> argLetter.append("I");
                case "bool" -> argLetter.append("Z");
                case "reel" -> argLetter.append("F");
            }
        }

        //System.out.println("args : " + argLetter + " | return : " + returnTypeLetter);

        method = container.visitMethod(ACC_PUBLIC, name,
                "(" + argLetter + ")" + returnTypeLetter, null, null);
        method.visitCode();

        //TODO LOAD ARGS FROM STACK

        // Ajouter les instructions ici...
        generateBlock(expression.getBody());

        switch (expression.getReturnType().getTypeSymbol()) {
            case "str" -> method.visitInsn(ARETURN);
            case "int", "bool" -> method.visitInsn(IRETURN);
            case "reel" -> method.visitInsn(FRETURN);
        }

        method.visitMaxs(-1, -1);
        method.visitEnd();
        method = null;
    }


    public void generateBinaryExpression(BinaryExpressionNode node) {
        System.out.println("BinaryExpression");

        ExpressionNode left = node.getLeft();
        ExpressionNode right = node.getRight();
        switch (node.getOperator().getKind()) {
            case PLUS -> {
                if (left.getTypeStr().equals("str")) {
                    if (left instanceof LiteralNode) {
                        numOperation(LADD, DADD, left, right);
                    } else if (left instanceof StringNode) {
                        System.out.println(left.getTypeStr());
                        String a = ((StringNode) left).getValue();
                        String b = ((StringNode) right).getValue();
                        System.out.println(a + b);
                        invokeStatic(method, RunTime.class, "concat", String.class, String.class);
                    }
                } else if (right.getTypeStr().equals("str")) {
                    invokeStatic(method, RunTime.class, "concat", String.class, String.class);
                } else {
                    numOperation(LADD, DADD, left, right);
                }
            }
            case STAR -> numOperation(LMUL, DMUL, left, right);
            case SLASH -> numOperation(LDIV, DDIV, left, right);
            case PERC -> numOperation(LREM, DREM, left, right);
            case MINUS -> numOperation(LSUB, DSUB, left, right);
            case EQEQ -> comparison(node.getOperator(), IFEQ, IF_ICMPEQ, IF_ACMPEQ, left, right);
            case DIFF -> comparison(node.getOperator(), IFNE, IF_ICMPNE, IF_ACMPNE, left, right);
            case MORE -> comparison(node.getOperator(), IFGT, -1, -1, left, right);
            case LESS -> comparison(node.getOperator(), IFLT, -1, -1, left, right);
            case MOREEQ -> comparison(node.getOperator(), IFGE, -1, -1, left, right);
            case LESSEQ -> comparison(node.getOperator(), IFLE, -1, -1, left, right);
        }
    }

    private void numOperation(int longOpcode, int doubleOpcode, ExpressionNode left, ExpressionNode right) {
        if (left.getTypeStr().equals("int") && right.getTypeStr().equals("int")) {
            String leftVal = ((NumberNode) left).getValue();
            String rightVal = ((NumberNode) right).getValue();
            method.visitLdcInsn(Integer.parseInt(leftVal));
            method.visitLdcInsn(Integer.parseInt(rightVal));
            method.visitInsn(longOpcode);
        } else if (left.getTypeStr().equals("real") && right.getTypeStr().equals("real")) {
            method.visitInsn(doubleOpcode);
        } else if (left.getTypeStr().equals("real") && right.getTypeStr().equals("int")) {
            method.visitInsn(L2D);
            method.visitInsn(doubleOpcode);
        } else if (left.getTypeStr().equals("int") && right.getTypeStr().equals("real")) {
            // in this case, we've added a L2D instruction before the long operand beforehand
            method.visitInsn(doubleOpcode);
        } else {
            throw new Error("Unexpected numeric operation type combination: " + left + ", " + right);
        }
    }

    public void comparison(Symbol op, int doubleWidthOpcode, int boolOpcode, int objOpcode,
                           ExpressionNode left, ExpressionNode right) {
        Label trueLabel = new Label();
        Label endLabel = new Label();
        System.out.println(left.getTypeStr());
        System.out.println(right.getTypeStr());
        if (left.getTypeStr().equals("int") && right.getTypeStr().equals("int")) {
            method.visitInsn(LCMP);
            method.visitJumpInsn(doubleWidthOpcode, trueLabel);
        } else if ((left.getTypeStr().equals("real") || left.getTypeStr().equals("int")) && right.getTypeStr().equals("real")) {
            // If left is an Int, we've added a L2D instruction before the long operand beforehand
            // Proper NaN handling: if NaN is involved, has to be false for all operations.
            int opcode = op.getKind() == LESS || op.getKind() == LESSEQ ? DCMPG : DCMPL;
            method.visitInsn(opcode);
            method.visitJumpInsn(doubleWidthOpcode, trueLabel);
        } else if (left.getTypeStr().equals("real") && right.getTypeStr().equals("int")) {
            method.visitInsn(L2D);
            // Proper NaN handling: if NaN is involved, has to be false for all operations.
            int opcode = op.getKind() == LESS || op.getKind() == LESSEQ ? DCMPG : DCMPL;
            method.visitInsn(opcode);
            method.visitJumpInsn(doubleWidthOpcode, trueLabel);
        } else if (left.getTypeStr().equals("bool") && right.getTypeStr().equals("bool")) {
            method.visitJumpInsn(boolOpcode, trueLabel);
        } else {
            method.visitJumpInsn(objOpcode, trueLabel);
        }

        method.visitInsn(ICONST_0);
        method.visitJumpInsn(GOTO, endLabel);
        method.visitLabel(trueLabel);
        method.visitInsn(ICONST_1);
        method.visitLabel(endLabel);
    }

}
