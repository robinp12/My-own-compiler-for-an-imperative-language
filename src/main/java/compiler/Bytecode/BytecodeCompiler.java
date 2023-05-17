package compiler.Bytecode;

import compiler.Lexer.Symbol;
import compiler.Parser.AST.*;
import compiler.Parser.AST.MethodNode;
import compiler.Semantic.SymbolTable;
import org.objectweb.asm.*;

import static compiler.Lexer.SymbolKind.LESS;
import static compiler.Lexer.SymbolKind.LESSEQ;
import static org.objectweb.asm.Opcodes.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class BytecodeCompiler {
    private ClassWriter container;
    /* MethodVisitor for current method. */
    private MethodVisitor method;
    private int idx = 0;

    public BytecodeCompiler(ProgramNode ast) {
        System.out.println("------ BYTECODE ------");
        // Class
        container = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        container.visit(V1_8, ACC_PUBLIC, "Main", null, "java/lang/Object", null);
        // Constructor
        method = container.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        method.visitCode();
        method.visitVarInsn(ALOAD, 0);
        method.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        method.visitInsn(RETURN);
        method.visitMaxs(1, 1);
        method.visitEnd();

        // Others methods
        root(ast);
        container.visitEnd();
    }

    public void getRender() {
        try (FileOutputStream fos = new FileOutputStream("Output.class")) {
            fos.write(getGeneration());
            System.out.println("Le bytecode a ete enregistre dans MyClass.class.");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'enregistrement du bytecode : " + e.getMessage());
        }
    }

    public byte[] getGeneration() {
        System.out.println(Arrays.toString(container.toByteArray()));
        return container.toByteArray();
    }

    public void root(ProgramNode node) {
        // Method main
        method = container.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        method.visitCode();

        if (node.getTypeStr().equals("root")) {
            for (ExpressionNode expression : node.getExpressions()) {
                generateExpression(expression);
            }
        }
        method.visitInsn(RETURN);
        method.visitMaxs(0, 0);
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
        }
        /*else if (expression instanceof AssignmentArrayNode) {
            visit((AssignmentArrayNode) expression);
        } else if (expression instanceof BooleanNode) {
            visit((BooleanNode) expression);
        } else if (expression instanceof LiteralNode) {
            visit((LiteralNode) expression);
        } else if (expression instanceof MethodCallNode) {
            visit((MethodCallNode) expression);
        } else if (expression instanceof NumberNode) {
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

    private void generateWhile(WhileStatementNode expression) {
        BinaryExpressionNode exp = (BinaryExpressionNode) expression.getCondition();

        Label loopStart = new Label();
        Label loopEnd = new Label();

        // Boucle while
        method.visitLabel(loopStart);

        // Condition left >= right
        generateBinaryExpression(exp);
        method.visitJumpInsn(IFEQ, loopEnd);

        SymbolTable st = new SymbolTable();
        System.out.println(st);

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
        StatementNode stmt = block.getStatements();
        generateStatement(stmt);
    }

    private void generateStatement(StatementNode expression) {
        System.out.println(expression);
        for (ExpressionNode statement : expression.getStatements()) {
            System.out.println("generate statement");
            generateExpression(statement);
        }
    }

    private void generateAssignment(AssignmentNode statement) {
        System.out.println("pas encore");
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
                System.out.println(expression);
                NumberNode val = (NumberNode) expression.getValue();
                if (val != null) {
                    method.visitLdcInsn(Integer.parseInt(val.getValue()));
                    method.visitVarInsn(ISTORE, idx++);
                } else {
                    method.visitLdcInsn(0);
                    method.visitVarInsn(ISTORE, idx++);
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
        String recordName = record.getIdentifier();
    }

    public void generateProc(MethodNode expression) {
        String name = expression.getIdentifier();
        String returnTypeLetter = "";
        // Return type
        switch (expression.getReturnType().getTypeSymbol()) {
            case "str":
                returnTypeLetter = "Ljava/lang/String;";
                break;
            case "int":
                returnTypeLetter = "I";
                break;
            case "bool":
                returnTypeLetter = "Z";
                break;
            default:
                returnTypeLetter = "V";

        }

        // Arguments type
        StringBuilder argLetter = new StringBuilder();
        for (ParamNode parameter : expression.getParameters()) {
            switch (parameter.getTypeStr()) {
                case "str":
                    argLetter.append("Ljava/lang/String;");
                    break;
                case "int":
                    argLetter.append("I");
                    break;
                case "bool":
                    argLetter.append("Z");
                    break;
            }
        }
        if (expression.getParameters().size() > 0) {
            for (ParamNode parameter : expression.getParameters()) {
                //System.out.println(parameter.getValue);
                //method.visitInsn(parameter.getValue); // Valeur pour les arguments
            }
        }

        // Appeler la methode dans main
        method.visitMethodInsn(INVOKESTATIC, "Main", name, "(" + argLetter + ")" + returnTypeLetter, false);
        method.visitInsn(POP);

        System.out.println("args : " + argLetter);
        System.out.println("return : " + returnTypeLetter);
        // Methode avec aucun argument
        method = container.visitMethod(ACC_PUBLIC | ACC_STATIC, name,
                "(" + argLetter + ")" + returnTypeLetter, null, null);
        method.visitCode();

        // Ajouter les instructions ici...
        generateBlock(expression.getBody());

        method.visitMaxs(0, 0);
        method.visitEnd();

    }


    public void generateBinaryExpression(BinaryExpressionNode node) {
        ExpressionNode left = node.getLeft();
        ExpressionNode right = node.getRight();
        switch (node.getOperator().getKind()) {
            case PLUS:
                if (left.getTypeStr().equals("str")) {
                    String a = ((StringNode) left).getValue();
                    String b = ((StringNode) right).getValue();
                    System.out.println(a + b);
                    //invokeStatic(method, RunTime.class, "concat", String.class, String.class);
                } else if (right.getTypeStr().equals("str")) {
                    //invokeStatic(method, RunTime.class, "concat", String.class, String.class);
                } else {
                    numOperation(LADD, DADD, left, right);
                }
                break;
            case STAR:
                numOperation(LMUL, DMUL, left, right);
                break;
            case SLASH:
                numOperation(LDIV, DDIV, left, right);
                break;
            case PERC:
                numOperation(LREM, DREM, left, right);
                break;
            case MINUS:
                numOperation(LSUB, DSUB, left, right);
                break;


            case EQEQ:
                comparison(node.getOperator(), IFEQ, IF_ICMPEQ, IF_ACMPEQ, left, right);
                break;
            case DIFF:
                comparison(node.getOperator(), IFNE, IF_ICMPNE, IF_ACMPNE, left, right);
                break;
            case MORE:
                comparison(node.getOperator(), IFGT, -1, -1, left, right);
                break;
            case LESS:
                comparison(node.getOperator(), IFLT, -1, -1, left, right);
                break;
            case MOREEQ:
                comparison(node.getOperator(), IFGE, -1, -1, left, right);
                break;
            case LESSEQ:
                comparison(node.getOperator(), IFLE, -1, -1, left, right);
                break;
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
