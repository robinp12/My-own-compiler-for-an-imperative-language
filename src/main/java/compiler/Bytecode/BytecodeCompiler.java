package compiler.Bytecode;

import compiler.Lexer.Symbol;
import compiler.Parser.AST.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static compiler.Bytecode.AsmUtils.invokeStatic;
import static compiler.Lexer.SymbolKind.LESS;
import static compiler.Lexer.SymbolKind.LESSEQ;
import static org.objectweb.asm.Opcodes.*;


public class BytecodeCompiler {
    private ClassWriter container;
    /* MethodVisitor for current method. */
    private MethodVisitor method;
    private int idx = 0;

    private Map<String, Integer> valueTable;
    private Map<String, ArrayList<ParamNode>> functionTable;

    public BytecodeCompiler(ProgramNode ast) {
        System.out.println("------ BYTECODE ------");
        this.valueTable = new HashMap<>();
        this.functionTable = new HashMap<>();

        // Class
        container = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        container.visit(V1_8, ACC_PUBLIC, "Main", null, "java/lang/Object", null);
        // Constructor
        method = container.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        method.visitCode();
        method.visitVarInsn(ALOAD, 0);
        method.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        method.visitInsn(RETURN);
        method.visitMaxs(0, 0);
        method.visitEnd();

        // Others methods
        root(ast);
        container.visitEnd();
        System.out.println("Fonction Table : " + functionTable.toString());
        System.out.println("Variable Table : " + valueTable.toString());
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
        functionTable.put("main", new ArrayList<>());
        method = container.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        method.visitCode();

        if (node.getTypeStr().equals("root")) {
            for (ExpressionNode expression : node.getExpressions()) {
                generateExpression(expression);
            }
        }
        method.visitInsn(RETURN);
        //method.visitMaxs(-1, -1);
        method.visitEnd();
    }

    public void generateExpression(ExpressionNode expression) {
        if (expression instanceof VarDeclarationNode) {
            generateVar(((VarDeclarationNode) expression).getAssignment());
        } else if (expression instanceof ValDeclarationNode) {
            generateVar(((ValDeclarationNode) expression).getAssignment());
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
            generateFor((ForStatementNode) expression);
        } else if (expression instanceof IfStatementNode) {
            generateIf((IfStatementNode) expression);
        } else if (expression instanceof AssignmentNode) {
            generateAssignment((AssignmentNode) expression);
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
        String identifier = expression.getIdentifier();
        ArrayList<ParamNode> function = functionTable.get(identifier);
        if (functionTable.containsKey(identifier)) {
            if (function.size() != expression.getParameters().size()) {
                try {
                    throw new Exception("Function " + identifier + " need " + function.size() + " argument(s)");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            for (int i = 0; i < function.size(); i++) {
                if(!function.get(i).getTypeStr().equals(expression.getParameters().get(i).getTypeStr())){
                    try {
                        throw new Exception("In function \"" + identifier +"\", argument \""+ function.get(i).getIdentifier() +"\" need to be \"" + function.get(i).getTypeStr()+"\" type");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    valueTable.put(function.get(i).getIdentifier(),Integer.parseInt(expression.getParameters().get(i).getIdentifier()));
                }
            }
        }

        System.out.println(valueTable);
        // Remove variable from the scope
        for (ParamNode parameter : function) {
            valueTable.remove(parameter.getIdentifier());
        }
    }

    private void generateWhile(WhileStatementNode expression) {
        System.out.println("while");
        BinaryExpressionNode exp = (BinaryExpressionNode) expression.getCondition();

        Label loopStart = new Label();
        Label loopEnd = new Label();

        // Boucle while
        method.visitLabel(loopStart);

        // Condition left >= right
        generateBinaryExpression(exp);
        method.visitJumpInsn(IFEQ, loopEnd);

        generateBlock(expression.getBlock());

        method.visitJumpInsn(GOTO, loopStart); // Revenir au début de la boucle
        method.visitLabel(loopEnd);
    }

    private void generateIf(IfStatementNode expression) {
        //BooleanNode valBool = (BooleanNode) expression.getCondition();
        System.out.println(expression.getCondition());
        BinaryExpressionNode stmt = (BinaryExpressionNode) expression.getCondition();
        boolean hasElse = expression.getElseStatements() != null;
        Label elseLabel = new Label();
        Label endLabel = new Label();

        generateBinaryExpression(stmt);
        method.visitJumpInsn(IFEQ, hasElse ? elseLabel : endLabel);
        generateBlock(expression.getThenStatements());
        if (hasElse) {
            method.visitJumpInsn(GOTO, endLabel);
            method.visitLabel(elseLabel);
            generateBlock(expression.getElseStatements());
        }
        method.visitLabel(endLabel);

    }

    private void generateFor(ForStatementNode expression) {
        NumberNode start = (NumberNode) expression.getStart();
        NumberNode step = (NumberNode) expression.getStep();
        NumberNode end = (NumberNode) expression.getEnd();
        int valStart = Integer.parseInt(start.getValue());
        int valStep = Integer.parseInt(step.getValue());
        int valEnd = Integer.parseInt(end.getValue());

        // Declare and initialize the variable 'i' with the value
        method.visitLdcInsn(valStart);
        method.visitVarInsn(ISTORE, 0);

        // Label for the beginning of the loop
        Label loopStart = new Label();
        method.visitLabel(loopStart);
        method.visitFrame(F_APPEND, 1, new Object[]{INTEGER}, 0, null);

        // Code inside the loop
        generateBlock(expression.getBlock());

        // Increment 'i'
        method.visitVarInsn(ILOAD, 0);
        method.visitLdcInsn(valStep);
        method.visitInsn(IADD);
        method.visitVarInsn(ISTORE, 0);

        // Condition for continuing the loop
        method.visitVarInsn(ILOAD, 0);
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

    private void generateAssignment(AssignmentNode statement) {
        System.out.println("pas encore : " + statement);
        if (valueTable.containsKey(statement.getIdentifier())) {
            int register = valueTable.get(statement.getIdentifier());
            System.out.println("var in table at register :" + valueTable.get(statement.getIdentifier()));
            method.visitVarInsn(ILOAD, register);
        }
    }

    private void generateReturn(ReturnNode statement) {
        if (statement.getValue().getTypeStr().equals("str")) {
            StringNode val = (StringNode) statement.getValue();
            method.visitLdcInsn(val.getValue());
            method.visitInsn(ARETURN);
        } else if (statement.getValue().getTypeStr().equals("binaryExp")) {
            generateBinaryExpression((BinaryExpressionNode) statement.getValue());
            method.visitInsn(RETURN);
        }

    }

    public void generateConst(ConstantDeclarationNode expression) {
        AssignmentNode assignment = expression.getAssignment();

        if (assignment.getType().getTypeStr().equals("str")) {
            StringNode val = (StringNode) assignment.getValue();
            container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "Ljava/lang/String;", null, val.getValue()).visitEnd();
        }
        if (assignment.getType().getTypeStr().equals("int")) {
            NumberNode val = (NumberNode) assignment.getValue();
            container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "I", null, Integer.parseInt(val.getValue())).visitEnd();
        }
        if (assignment.getType().getTypeStr().equals("bool")) {
            BooleanNode val = (BooleanNode) assignment.getValue();
            System.out.println(val.isVal());
            container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "Z", null, val.isVal()).visitEnd();
        }
        if (assignment.getType().getTypeStr().equals("real")) {
            NumberNode val = (NumberNode) assignment.getValue();
            System.out.println(val.getValue());
            container.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, assignment.getIdentifier(), "F", null, Float.parseFloat(val.getValue())).visitEnd();
        }
    }

    public void generateVar(AssignmentNode expression) {
        System.out.println("var || val byte");
        String type = expression.getTypeStr();
        if (type.equals("int")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof AssignmentArrayNode) {
                // Case of array
                AssignmentArrayNode val = (AssignmentArrayNode) expression.getValue();
                NumberNode size = (NumberNode) val.getSize();
                method.visitIntInsn(BIPUSH, Integer.parseInt(size.getValue()));
                method.visitIntInsn(NEWARRAY, T_INT);
                method.visitVarInsn(ASTORE, idx++);
            } else {
                NumberNode val = (NumberNode) expression.getValue();
                if (val != null) {
                    System.out.println(expression);
                    idx++;
                    valueTable.put(expression.getIdentifier(), idx);
                    method.visitLdcInsn(Integer.parseInt(val.getValue()));
                    method.visitVarInsn(ISTORE, idx);
                } else {
                    idx++;
                    valueTable.put(expression.getIdentifier(), idx);
                    method.visitLdcInsn(0);
                    method.visitVarInsn(ISTORE, idx);
                }
            }
        }
        if (type.equals("str")) {
            StringNode val = (StringNode) expression.getValue();
            method.visitLdcInsn(val.getValue());
            method.visitVarInsn(ASTORE, idx++);
        }
        if (type.equals("bool")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof AssignmentArrayNode) {
                // Case of array
                AssignmentArrayNode val = (AssignmentArrayNode) expression.getValue();
                NumberNode size = (NumberNode) val.getSize();
                method.visitIntInsn(BIPUSH, Integer.parseInt(size.getValue()));
                method.visitIntInsn(NEWARRAY, T_BOOLEAN);
                method.visitVarInsn(ASTORE, idx++);
            } else {
                BooleanNode val = (BooleanNode) expression.getValue();
                int isTrue = val.isVal() ? ICONST_1 : ICONST_0;
                method.visitInsn(isTrue);
                method.visitVarInsn(ISTORE, idx++);
            }
        }
        if (type.equals("real")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof AssignmentArrayNode) {
                // Case of array
                AssignmentArrayNode val = (AssignmentArrayNode) expression.getValue();
                NumberNode size = (NumberNode) val.getSize();
                method.visitIntInsn(BIPUSH, Integer.parseInt(size.getValue()));
                method.visitIntInsn(NEWARRAY, T_FLOAT);
                method.visitVarInsn(ASTORE, idx++);
            } else {
                NumberNode val = (NumberNode) expression.getValue();
                System.out.println(val.getValue());
                method.visitLdcInsn(Float.parseFloat(val.getValue()));
                method.visitVarInsn(FSTORE, idx++);
            }
        }
    }

    public void generateRecord(RecordNode record) {
        System.out.println("pas encore : " + record);
    }

    public void generateProc(MethodNode expression) {
        String name = expression.getIdentifier();
        String returnTypeLetter = switch (expression.getReturnType().getTypeSymbol()) {
            case "str" -> "Ljava/lang/String;";
            case "int" -> "I";
            case "bool" -> "Z";
            default -> "V";
        };
        // Return type

        // Arguments type
        StringBuilder argLetter = new StringBuilder();
        for (ParamNode parameter : expression.getParameters()) {
            // Add variable in the scope
            valueTable.put(parameter.getIdentifier(), 0);

            switch (parameter.getTypeStr()) {
                case "str" -> argLetter.append("Ljava/lang/String;");
                case "int" -> argLetter.append("I");
                case "bool" -> argLetter.append("Z");
            }
        }
        if (expression.getParameters().size() > 0) {
            for (ParamNode parameter : expression.getParameters()) {
                //method.visitInsn(parameter.getValue); // Valeur pour les arguments
            }
        }
        // Appeler la methode dans main
        method.visitMethodInsn(INVOKESTATIC, "Main", name, "(" + argLetter + ")" + returnTypeLetter, false);
        method.visitInsn(POP);

        System.out.println("args : " + argLetter + " | return : " + returnTypeLetter);

        functionTable.put(name, expression.getParameters());
        method = container.visitMethod(ACC_PUBLIC | ACC_STATIC, name,
                "(" + argLetter + ")" + returnTypeLetter, null, null);
        method.visitCode();
        // Ajouter les instructions ici...
        generateBlock(expression.getBody());

        method.visitMaxs(0, 0);
        method.visitEnd();

    }


    public void generateBinaryExpression(BinaryExpressionNode node) {
        System.out.println("BinaryExpression");

        ExpressionNode left = node.getLeft();
        ExpressionNode right = node.getRight();
        switch (node.getOperator().getKind()) {
            case PLUS -> {
                if (left.getTypeStr().equals("str")) {
                    if (left instanceof LiteralNode) {

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
            method.visitIntInsn(BIPUSH, Integer.parseInt(leftVal));
            method.visitIntInsn(BIPUSH, Integer.parseInt(rightVal));
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
        method.visitInsn(IRETURN);
    }

    public void comparison(Symbol op, int doubleWidthOpcode, int boolOpcode, int objOpcode,
                           ExpressionNode left, ExpressionNode right) {
        Label trueLabel = new Label();
        Label endLabel = new Label();
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
