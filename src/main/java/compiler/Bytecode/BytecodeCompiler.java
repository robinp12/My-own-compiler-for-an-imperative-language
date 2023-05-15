package compiler.Bytecode;

import compiler.Lexer.Symbol;
import compiler.Parser.AST.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static compiler.Bytecode.AsmUtils.invokeStatic;
import static org.objectweb.asm.Opcodes.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class BytecodeCompiler {
    private ClassWriter container;
    /* MethodVisitor for current method. */
    private MethodVisitor method;

    public BytecodeCompiler(ProgramNode ast) {
        System.out.println("------ BYTECODE ------");
        container = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        container.visit(V1_8, ACC_PUBLIC, "main", null, "java/lang/Object", null);
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
        System.out.println("root byte");

        method = container.visitMethod(ACC_PUBLIC | ACC_STATIC, "<init>",
                "()V", null, null);
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
            generateVar((VarDeclarationNode) expression);
        } else if (expression instanceof ValDeclarationNode) {
            generateVal((ValDeclarationNode) expression);
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
        }
       /* } else if (expression instanceof AssignmentNode) {
            visit((AssignmentNode) expression);
        } else if (expression instanceof AssignmentArrayNode) {
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
        } else if (expression instanceof ReturnNode) {
            visit((ReturnNode) expression);
        } else if (expression instanceof StringNode) {
            visit((StringNode) expression);
        } else if (expression instanceof TypeNode) {
            visit((TypeNode) expression);
        } else if (expression instanceof ValueNode<?>) {
            visit((ValueNode) expression);
        } else if (expression instanceof WhileStatementNode) {
            visit((WhileStatementNode) expression);
        }*/

    }

    private void generateIf(IfStatementNode expression) {
        BinaryExpressionNode stmt = (BinaryExpressionNode) expression.getCondition();
        int valCond = 10;
        boolean hasElse = expression.getElseStatements() != null;

        Label elseLabel = new Label();
        Label endLabel = new Label();

        // Déclarer et initialiser la constante 'v'
        method.visitIntInsn(BIPUSH, 10);
        method.visitVarInsn(ISTORE, 0);

        // Charger la valeur de 'v' sur la pile
        method.visitVarInsn(ILOAD, 0);

        // Comparer la valeur avec 10
        method.visitIntInsn(BIPUSH, valCond);
        method.visitJumpInsn(IF_ICMPNE, elseLabel);

        // Si la comparaison est vraie, mettre 'true' sur la pile et retourner
        method.visitInsn(ICONST_1);
        method.visitInsn(IRETURN);

        // Sinon, mettre 'false' sur la pile et retourner
        method.visitLabel(elseLabel);
        method.visitInsn(ICONST_0);
        method.visitInsn(IRETURN);

        method.visitMaxs(2, 1);
        method.visitEnd();
    }

    private void generateFor(ForStatementNode expression) {
        NumberNode star = (NumberNode) expression.getStep();
        System.out.println(star);
        //int step = expression.getStep();
        //int end = expression.getEnd();
        int start = 1;
        int end = 100;
        int step = 2;
        // Declare and initialize the variable 'i' with the value 1
        method.visitIntInsn(BIPUSH, start);
        method.visitVarInsn(ISTORE, 0);

        // Label for the beginning of the loop
        Label loopStart = new Label();
        method.visitLabel(loopStart);
        method.visitFrame(F_APPEND, 1, new Object[]{INTEGER}, 0, null);

        // Code inside the loop
        generateBlock(expression.getBlock());

        // Increment 'i'
        method.visitVarInsn(ILOAD, 0);
        method.visitIntInsn(BIPUSH, step);
        method.visitInsn(IADD);
        method.visitVarInsn(ISTORE, 0);

        // Condition for continuing the loop
        method.visitVarInsn(ILOAD, 0);
        method.visitIntInsn(BIPUSH, end);
        Label loopEnd = new Label();
        method.visitJumpInsn(IF_ICMPLE, loopStart);

        method.visitLabel(loopEnd);
        method.visitFrame(F_CHOP, 1, null, 0, null);

        method.visitInsn(RETURN);

        method.visitMaxs(2, 1);
        method.visitEnd();

        method.visitEnd();
    }

    private void generateBlock(BlockNode block) {

        StatementNode stmt = block.getStatements();
        generateStatement(stmt);
    }

    private void generateStatement(StatementNode expression) {
        for (ExpressionNode statement : expression.getStatements()) {
            generateReturn((ReturnNode) statement);
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

    }

    public void generateVar(VarDeclarationNode expression) {

    }

    public void generateVal(ValDeclarationNode expression) {
        System.out.println("val byte");
        String type = expression.getAssignment().getTypeStr();
        System.out.println(type);
        if (type.equals("int")) {
            NumberNode val = (NumberNode) expression.getAssignment().getValue();
            System.out.println(Integer.parseInt(val.getValue()));
            method.visitCode();
            method.visitIntInsn(BIPUSH, Integer.parseInt(val.getValue()));
            method.visitVarInsn(ISTORE, 0);
        }
        if (type.equals("str")) {
            StringNode val = (StringNode) expression.getAssignment().getValue();
            System.out.println(val.getValue());
            method.visitLdcInsn(val.getValue());
            method.visitVarInsn(ASTORE, 0);
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

        System.out.println("args : " + argLetter);
        System.out.println("return : " + returnTypeLetter);
        // Methode avec aucun argument
        method = container.visitMethod(ACC_PUBLIC | ACC_STATIC, name,
                "(" + argLetter + ")" + returnTypeLetter, null, null);
        method.visitCode();

        // Ajouter les instructions ici...
        generateBlock(expression.getBody());

        method.visitMaxs(expression.getParameters().size(), expression.getParameters().size());
        method.visitEnd();

    }


    public void generateBinaryExpression(BinaryExpressionNode node) {
        ExpressionNode left = node.getLeft();
        ExpressionNode right = node.getRight();
        switch (node.getOperator().getKind()) {
            case PLUS:
                if (node.getLeft().getTypeStr().equals("str")) {
                    invokeStatic(method, RunTime.class, "concat", String.class, String.class);
                } else if (node.getRight().getTypeStr().equals("str")) {
                    invokeStatic(method, RunTime.class, "concat", String.class, String.class);
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
            throw new Error("unexpected numeric operation type combination: " + left + ", " + right);
        }
        method.visitInsn(IRETURN);
    }

    public void comparison(Symbol op, int doubleWidthOpcode, int boolOpcode, int objOpcode,
                           ExpressionNode left, ExpressionNode right) {
        if (left.getTypeStr().equals("int") && right.getTypeStr().equals("int")) {
            method.visitInsn(LCMP);
        }
    }

}
