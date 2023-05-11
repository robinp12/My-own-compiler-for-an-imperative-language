package compiler.Bytecode;

import compiler.Lexer.Symbol;
import compiler.Parser.AST.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static compiler.Bytecode.AsmUtils.invokeStatic;
import static org.objectweb.asm.Opcodes.*;


public class BytecodeCompiler {
    private final ProgramNode ast;
    private ClassWriter container;
    /* MethodVisitor for current method. */
    private MethodVisitor method;

    public BytecodeCompiler(ProgramNode ast) {
        this.ast = ast;
        container = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        container.visit(V1_8, ACC_PUBLIC, "main", null, "java/lang/Object", null);
        root(ast);
    }

    public void root(ProgramNode node) {
        if (node.getTypeStr().equals("root")) {
            for (ExpressionNode expression : node.getExpressions()) {
                generateExpression(expression);
            }
        }
    }

    public void generateExpression(ExpressionNode expression) {
        System.out.println("expression");
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
        }
       /* } else if (expression instanceof AssignmentNode) {
            visit((AssignmentNode) expression);
        } else if (expression instanceof AssignmentArrayNode) {
            visit((AssignmentArrayNode) expression);
        } else if (expression instanceof BlockNode) {
            visit((BlockNode) expression);
        } else if (expression instanceof BooleanNode) {
            visit((BooleanNode) expression);
        } else if (expression instanceof ForStatementNode) {
            visit((ForStatementNode) expression);
        } else if (expression instanceof IfStatementNode) {
            visit((IfStatementNode) expression);
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

    private void generateStatement(StatementNode expression) {

    }

    public void generateConst(ConstantDeclarationNode expression) {

    }

    public void generateVar(VarDeclarationNode expression) {

    }

    public void generateVal(ValDeclarationNode expression) {

    }

    public void generateRecord(RecordNode record) {
        String recordName = record.getIdentifier();
        // Methode avec aucun argument et qui ne retourne rien (void)
        if (record.getFields().size() == 0 && record.getTypeStr().equals("void")) {
            method = container.visitMethod(ACC_PUBLIC | ACC_STATIC, recordName,
                    "()V", null, null);
            method.visitCode();
            // Ajouter les instructions ici...
            method.visitInsn(RETURN);
            method.visitMaxs(0, 0);
            method.visitEnd();
        }
        // Methode avec un seul argument de type int et qui retourne un int
        if (record.getFields().size() == 1 && record.getTypeStr().equals("int")) {
            method = container.visitMethod(ACC_PUBLIC | ACC_STATIC, recordName,
                    "(I)I", null, null);
            method.visitCode();
            // Ajouter les instructions ici...
            method.visitInsn(IRETURN);
            method.visitMaxs(1, 1);
            method.visitEnd();
        }
        method.visitEnd();
    }

    public void generateProc(MethodNode expression) {

    }


    public void generateBinaryExpression(BinaryExpressionNode node) {
        TypeNode left = (TypeNode) node.getLeft();
        TypeNode right = (TypeNode) node.getRight();

        switch (node.getOperator().getKind()) {
            case PLUS:
                if (node.getLeft().getTypeStr().equals("Str")) {
                    invokeStatic(method, RunTime.class, "concat", String.class, String.class);
                } else if (node.getRight().getTypeStr().equals("Str")) {
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

    private void numOperation(int longOpcode, int doubleOpcode, TypeNode left, TypeNode right) {
        if (left.getTypeStr().equals("int") && right.getTypeStr().equals("int")) {
            method.visitInsn(longOpcode);
        } else if (left.getTypeStr().equals("real") && right.getTypeStr().equals("real")) {
            method.visitInsn(doubleOpcode);
        } else if (left.getTypeStr().equals("real") && right.getTypeStr().equals("int")) {
            method.visitInsn(L2D);
            method.visitInsn(doubleOpcode);
        } else if (left.getTypeStr().equals("int") && right.getTypeStr().equals("float")) {
            // in this case, we've added a L2D instruction before the long operand beforehand
            method.visitInsn(doubleOpcode);
        } else {
            throw new Error("unexpected numeric operation type combination: " + left + ", " + right);
        }

    }

    public void comparison(Symbol op, int doubleWidthOpcode, int boolOpcode, int objOpcode,
                           TypeNode left, TypeNode right) {
        if (left.getTypeStr().equals("INT") && right.getTypeStr().equals("INT")) {
            method.visitInsn(LCMP);
        }
    }

}
