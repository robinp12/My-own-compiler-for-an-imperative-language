package compiler.Bytecode;

import compiler.Compiler;
import compiler.Lexer.Symbol;
import compiler.Parser.AST.*;
import compiler.Semantic.SemanticAnalyzer;
import org.objectweb.asm.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static compiler.Lexer.SymbolKind.LESS;
import static compiler.Lexer.SymbolKind.LESSEQ;
import static org.objectweb.asm.Opcodes.*;


public class BytecodeCompiler {
    private final ClassWriter container;
    private ByteArrayClassLoader loader;
    /* MethodVisitor for current method. */
    private MethodVisitor methodMain;
    private MethodVisitor method;
    private int idx = 10;
    private final Map<String, Integer> valueTable;
    private StringBuilder argLetter;
    private String returnTypeLetter;

    public BytecodeCompiler(ProgramNode ast) {
        this.valueTable = new HashMap<>();
        this.loader = new ByteArrayClassLoader();

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
        if (expression instanceof VarDeclarationNode) {
            generateVar(((VarDeclarationNode) expression).getAssignment(), (method == null) ? methodMain : method);
        } else if (expression instanceof ValDeclarationNode) {
            generateVar(((ValDeclarationNode) expression).getAssignment(), (method == null) ? methodMain : method);
        } else if (expression instanceof ConstantDeclarationNode) {
            generateConst((ConstantDeclarationNode) expression);
        } else if (expression instanceof RecordNode) {
            generateRecord((RecordNode) expression);
        } else if (expression instanceof StatementNode) {
            generateStatement(null,(StatementNode) expression);
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
            generateWhile((WhileStatementNode) expression, (method == null) ? methodMain : method);
        } else if (expression instanceof ReturnNode) {
            generateReturn(null,(ReturnNode) expression);
        } else if (expression instanceof MethodCallNode) {
            generateProcCall((MethodCallNode) expression);
        }else if (expression instanceof BlockNode){
            generateBlock(null,(BlockNode) expression);
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
        method = method == null ? methodMain : method;

        String name = expression.getIdentifier();

        if (SemanticAnalyzer.functions.contains(name)) {
            generateBuiltInt(expression);
            return;
        }
        StringBuilder argLetterCall = new StringBuilder();
        for (ParamNode parameter : expression.getParameters()) {
            switch (parameter.getTypeStr()) {
                case "str" -> argLetterCall.append("Ljava/lang/String;");
                case "int" -> argLetterCall.append("I");
                case "bool" -> argLetterCall.append("Z");
                case "real" -> argLetterCall.append("F");
            }
        }
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
        methodMain.visitMethodInsn(INVOKESTATIC, "Main", name, "(" + argLetterCall + ")" + returnTypeLetter, false);
        if(!returnTypeLetter.equals("V")){
            methodMain.visitInsn(POP);
        }
    }

    private void generateBuiltInt(MethodCallNode expression) {
        method = method == null ? methodMain : method;

        String name = expression.getIdentifier();
        ArrayList<ParamNode> params = expression.getParameters();

        switch (name) {
            case "not" -> {
                String resultat = params.get(0).getIdentifier().equals("true") ? "false" : "true";
                int isTrue = resultat.equals("true") ? ICONST_1 : ICONST_0;

                ++idx;
                valueTable.put(name + "_func", idx);
                method.visitInsn(isTrue);
                method.visitVarInsn(ISTORE, idx);
            }
            case "chr" -> {
                int val = Integer.parseInt(params.get(0).getIdentifier());
                String resultat = Character.toString((char) val);
                ++idx;
                valueTable.put(name + "_func", idx);
                method.visitLdcInsn(resultat);
                method.visitVarInsn(ASTORE, idx);
            }
            case "len" -> {
                String str = params.get(0).getIdentifier();

                idx += 10;
                ++idx;
                valueTable.put(name + "_func", idx);
                method.visitLdcInsn(str.length());
                method.visitVarInsn(ISTORE, idx);
            }
            case "floor" -> {
                float val = Float.parseFloat(params.get(0).getIdentifier());
                int intValue = (int) Math.floor(val);

                ++idx;
                valueTable.put(name + "_func", idx);
                method.visitLdcInsn(intValue);
                method.visitVarInsn(ISTORE, idx);
            }
            case "readint" -> {
                ++idx;
                valueTable.put(name + "_func", idx);
                method.visitLdcInsn(Integer.parseInt(Compiler.argu[1]));
                try {
                    method.visitLdcInsn(Integer.parseInt(Compiler.argu[1]));
                } catch (RuntimeException e) {
                    method.visitLdcInsn(0);
                }
                method.visitVarInsn(ISTORE, idx);
            }
            case "readreal" -> {
                ++idx;
                valueTable.put(name + "_func", idx);
                try {
                    method.visitLdcInsn(Float.parseFloat(Compiler.argu[1]));
                } catch (RuntimeException e) {
                    method.visitLdcInsn(0.0F);
                }
                method.visitVarInsn(FSTORE, idx);
            }
            case "readstring" -> {
                ++idx;
                valueTable.put(name + "_func", idx);
                try {
                    method.visitLdcInsn(Compiler.argu[1]);
                } catch (RuntimeException e) {
                    method.visitLdcInsn("");
                }
                method.visitVarInsn(ASTORE, idx);
            }
            case "writeint", "write", "writereal", "writeln" -> {
                method.visitLdcInsn(params.get(0).getIdentifier());
                method.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                method.visitInsn(SWAP);
                method.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
        }
    }

    private void generateWhile(WhileStatementNode expression, MethodVisitor method) {
        BinaryExpressionNode exp = (BinaryExpressionNode) expression.getCondition();

        Label loopStart = new Label();
        Label loopEnd = new Label();

        // Loop while
        method.visitLabel(loopStart);

        // Condition left >= right
        generateBinaryExpression(exp);
        method.visitJumpInsn(IFEQ, loopEnd);

        generateBlock(null,expression.getBlock());

        method.visitJumpInsn(GOTO, loopStart); // Revenir au début de la boucle
        method.visitLabel(loopEnd);
    }

    private void generateIf(IfStatementNode expression) {
        method = method == null ? methodMain : method;

        //BooleanNode valBool = (BooleanNode) expression.getCondition();
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

        generateBlock(null,expression.getThenStatements());
        if (hasElse) {
            method.visitJumpInsn(GOTO, endLabel);
            method.visitLabel(elseLabel);
            generateBlock(null,expression.getElseStatements());
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
        generateBlock(null,expression.getBlock());

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

    private void generateBlock(ArrayList<ParamNode> params, BlockNode block) {
        StatementNode stmt = block.getStatements();
        if (stmt != null) {
            generateStatement(params,stmt);
        }
    }

    private void generateStatement(ArrayList<ParamNode> params,StatementNode expression) {
        for (ExpressionNode statement : expression.getStatements()) {
            if(statement instanceof ReturnNode){
                generateReturn(params,(ReturnNode) statement);
            }else{
                generateExpression(statement);
            }
        }
    }

    private void generateAssignment(AssignmentNode statement, MethodVisitor method) {
        if (valueTable.containsKey(statement.getIdentifier())) {
            int register = valueTable.get(statement.getIdentifier());
            switch (statement.getType().getTypeStr()) {
                case "real" -> {
                    if (statement.getValue() instanceof AssignmentArrayNode) {
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        NumberNode value = (NumberNode) array.getValue();
                        NumberNode index = (NumberNode) array.getIndex();

                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        method.visitLdcInsn(Float.parseFloat(value.getValue()));
                        method.visitInsn(FASTORE);
                    } else {
                        NumberNode value = (NumberNode) statement.getValue();
                        method.visitLdcInsn(Float.parseFloat(value.getValue()));
                        method.visitVarInsn(FSTORE, register);
                    }
                }
                case "int" -> {
                    if (statement.getValue() instanceof AssignmentArrayNode) {
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        NumberNode value = (NumberNode) array.getValue();
                        NumberNode index = (NumberNode) array.getIndex();

                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        method.visitLdcInsn(Integer.parseInt(value.getValue()));
                        method.visitInsn(IASTORE);
                    } else {
                        NumberNode value = (NumberNode) statement.getValue();
                        method.visitLdcInsn(Integer.parseInt(value.getValue()));
                        method.visitVarInsn(ISTORE, register);
                    }
                }
                case "bool" -> {
                    if (statement.getValue() instanceof AssignmentArrayNode) {
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        BooleanNode value = (BooleanNode) array.getValue();
                        NumberNode index = (NumberNode) array.getIndex();

                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        int isTrue = value.isVal() ? ICONST_1 : ICONST_0;
                        method.visitInsn(isTrue);
                        method.visitInsn(IASTORE);
                    } else {
                        BooleanNode value = (BooleanNode) statement.getValue();
                        int isTrue = value.isVal() ? ICONST_1 : ICONST_0;
                        method.visitInsn(isTrue);
                        method.visitVarInsn(ISTORE, register);
                    }
                }
                case "str" -> {
                    if (statement.getValue() instanceof AssignmentArrayNode) {
                        AssignmentArrayNode array = (AssignmentArrayNode) statement.getValue();
                        NumberNode index = (NumberNode) array.getIndex();
                        StringNode value = (StringNode) array.getValue();
                        method.visitVarInsn(ALOAD, register);
                        method.visitIntInsn(BIPUSH, Integer.parseInt(index.getValue()));
                        method.visitLdcInsn(value.getValue());
                        method.visitInsn(ASTORE);
                    } else {
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

    private void generateReturn(ArrayList<ParamNode> params,ReturnNode statement) {
        ExpressionNode val = statement.getValue();
        if (val instanceof NumberNode) {
            NumberNode value = (NumberNode) statement.getValue();
            method.visitLdcInsn(Integer.parseInt(value.getValue()));
            method.visitInsn(IRETURN);
        } else if (val instanceof LiteralNode) {
            String leftId = ((LiteralNode) val).getLiteral();
            for (int i = 0; i < params.size(); i++) {
                if(params.get(i).getIdentifier().equals(leftId)) {
                    method.visitVarInsn(ILOAD, i+1); // load
                }else{
                    int register = valueTable.get(leftId);
                    method.visitVarInsn(ILOAD, register);
                }
            }
            method.visitInsn(IRETURN);

        } else if (val.getTypeStr().equals("str")) {
            StringNode value = (StringNode) statement.getValue();
            method.visitLdcInsn(value.getValue());
            method.visitInsn(ARETURN);
        } else if (val.getTypeStr().equals("binaryExp")) {
            BinaryExpressionNode value = (BinaryExpressionNode) val;
            if(value.getLeft() instanceof LiteralNode && value.getRight() instanceof LiteralNode){
                String idLeft = ((LiteralNode) value.getLeft()).getLiteral();
                String idRight = ((LiteralNode) value.getRight()).getLiteral();
                for (int i = 0; i < params.size(); i++) {
                    if(params.get(i).getIdentifier().equals(idLeft)) {
                        method.visitVarInsn(ILOAD, i+1); // load
                    }
                    if(params.get(i).getIdentifier().equals(idRight)) {
                        method.visitVarInsn(ILOAD, i+1); // load
                    }
                }
                switch (value.getOperator().getKind()){
                    case PLUS ->
                        method.visitInsn(LADD); //operation
                    case STAR ->
                            method.visitInsn(IMUL); //operation
                    case SLASH ->
                            method.visitInsn(IDIV); //operation
                    case PERC ->
                            method.visitInsn(IREM); //operation
                    case MINUS ->
                            method.visitInsn(ISUB); //operation
                }
                method.visitInsn(IRETURN);
            }else{
                generateBinaryExpression((BinaryExpressionNode) statement.getValue());
            }
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
        String type = expression.getTypeStr();
        if (type.equals("int")) {
            ExpressionNode valType = expression.getValue();
            if (valType instanceof BinaryExpressionNode) {
                BinaryExpressionNode exp = (BinaryExpressionNode) valType;
                generateBinaryExpression(exp);
            } else if (valType instanceof MethodCallNode) {
                // Case of method call
                MethodCallNode val = (MethodCallNode) expression.getValue();

                if (SemanticAnalyzer.functions.contains(val.getIdentifier())) {
                    generateBuiltInt(val);
                    return;
                } else {
                    generateProcCall(val);
                }

            } else if (valType instanceof LiteralNode) {
                // Case of method call
                LiteralNode literal = (LiteralNode) expression.getValue();
                int register = valueTable.get(literal.getLiteral());
                method.visitVarInsn(ILOAD, register);
                ++idx;
                method.visitVarInsn(ISTORE, idx);
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
                method.visitTypeInsn(ANEWARRAY, "java/lang/String");
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
            }else if(valType instanceof BinaryExpressionNode){
                BinaryExpressionNode val = (BinaryExpressionNode) expression.getValue();
                if (val.getResult() != null){
                    ++idx;
                    valueTable.put(expression.getIdentifier(), idx);
                    int isTrue = Boolean.parseBoolean(val.getResult()) ? ICONST_1 : ICONST_0;
                    method.visitInsn(isTrue);
                }
            }
            else {
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
            } else if(valType instanceof BinaryExpressionNode){
                BinaryExpressionNode val = (BinaryExpressionNode) expression.getValue();
                if (val.getResult() != null){
                    ++idx;
                    valueTable.put(expression.getIdentifier(), idx);
                    method.visitLdcInsn(Float.parseFloat(val.getResult()));
                    method.visitVarInsn(FSTORE, idx);
                }
            }else {
                NumberNode val = (NumberNode) expression.getValue();
                ++idx;
                valueTable.put(expression.getIdentifier(), idx);
                method.visitLdcInsn(Float.parseFloat((val != null) ? val.getValue() : "0"));
                method.visitVarInsn(FSTORE, idx);

            }
        }
    }

    public void generateRecord(RecordNode record) {
        String id = record.getIdentifier();
        String name = id.substring(0, 1).toUpperCase() + id.substring(1);
        RecordAsm.generateRecord(name,record,loader);
    }

    public void generateProc(MethodNode expression) {
        String name = expression.getIdentifier();
        //TODO Create table for args linked to function
        returnTypeLetter = switch (expression.getReturnType().getTypeSymbol()) {
            case "str" -> "Ljava/lang/String;";
            case "int" -> "I";
            case "bool" -> "Z";
            case "real" -> "F";
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
                case "real" -> argLetter.append("F");
            }
        }

        method = container.visitMethod(ACC_PUBLIC, name,
                "(" + argLetter + ")" + returnTypeLetter, null, null);
        method.visitCode();

        //TODO LOAD ARGS FROM STACK
        // Ajouter les instructions ici...
        generateBlock(expression.getParameters(),expression.getBody());

        switch (expression.getReturnType().getTypeSymbol()) {
            case "str" -> method.visitInsn(ARETURN);
            case "int", "bool" -> method.visitInsn(IRETURN);
            case "real" -> method.visitInsn(FRETURN);
        }

        method.visitMaxs(-1, -1);
        method.visitEnd();
        method = null;
    }


    public void generateBinaryExpression(BinaryExpressionNode node) {

        ExpressionNode left = node.getLeft();
        ExpressionNode right = node.getRight();
        if (left instanceof BinaryExpressionNode){
            generateBinaryExpression((BinaryExpressionNode) left);
        }
        if (right instanceof BinaryExpressionNode){
            generateBinaryExpression((BinaryExpressionNode) right);
        }
        switch (node.getOperator().getKind()) {
            case PLUS -> {
                if (left.getTypeStr().equals("str")) {
                    if (left instanceof LiteralNode) {
                        numOperation(LADD, DADD, left, right);
                    } else if (left instanceof StringNode) {
                        String a = ((StringNode) left).getValue();
                        String b = ((StringNode) right).getValue();
                    }
                } else if (right.getTypeStr().equals("str")) {
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
        } else if (left instanceof LiteralNode && right instanceof LiteralNode) {
            int registerLeft = valueTable.get(((LiteralNode) left).getLiteral());
            int registerRight = valueTable.get(((LiteralNode) right).getLiteral());

            method.visitVarInsn(ILOAD, registerLeft); // load left
            method.visitVarInsn(ILOAD, registerRight); // load right
            method.visitInsn(doubleOpcode); //operation

            ++idx;
            method.visitVarInsn(ISTORE, idx); // store result in variable a
        } else if (left instanceof LiteralNode) {
            int registerLeft = valueTable.get(((LiteralNode) left).getLiteral());
            method.visitVarInsn(ILOAD, registerLeft); // load left
            if (right instanceof NumberNode){
                try{
                    method.visitLdcInsn(Integer.parseInt(((NumberNode) right).getValue())); // load right
                }catch (NumberFormatException e){
                    method.visitLdcInsn(Float.parseFloat(((NumberNode) right).getValue())); // load right
                }
            }else{
                method.visitLdcInsn(((StringNode) right).getValue()); // load right
            }
            method.visitInsn(doubleOpcode); //operation

            ++idx;
            method.visitVarInsn(ISTORE, idx); // store result in variable a
        } else {
            throw new Error("Unexpected numeric operation type combination: " + left + ", " + right);
        }
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
