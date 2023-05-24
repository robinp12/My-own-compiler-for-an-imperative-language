package compiler.Bytecode;

import compiler.Parser.AST.ParamNode;
import compiler.Parser.AST.RecordNode;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.*;

/**
 * Utilities to help emitting JVM bytecode.
 */
public final class RecordAsm {
    private static String returnTypeLetter;
    private static StringBuilder argLetter;

    private RecordAsm() {
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Alias for {@link Opcodes#IFEQ} that makes the purpose clearer when checking for a zero value
     * outside of the context of number comparison.
     */
    public final static int IF_ZERO = IFEQ;

    /**
     * Alias for {@link Opcodes#IFNE} that makes the purpose clearer when checking for a zero value
     * outside of the context of number comparison.
     */
    public final static int IF_NOT_ZERO = IFNE;

    public static void generateRecord(String name, RecordNode record, ByteArrayClassLoader loader) {
        ClassWriter container;

        container = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        container.visit(V1_8, ACC_PUBLIC, name, null, "java/lang/Object", null);

        ArrayList<ParamNode> recordParameters = record.getFields();
        argLetter = new StringBuilder();
        for (ParamNode recordParameter : recordParameters) {
            String id = recordParameter.getIdentifier();
            String type = recordParameter.getTypeStr();
            System.out.println(recordParameter);
            switch (type) {
                case "str" -> argLetter.append("Ljava/lang/String;");
                case "int" -> argLetter.append("I");
                case "bool" -> argLetter.append("Z");
                case "real" -> argLetter.append("F");
                case "str_array" -> argLetter.append("[Ljava/lang/String;");
                case "int_array" -> argLetter.append("[I");
                case "bool_array" -> argLetter.append("[Z");
                case "real_array" -> argLetter.append("[F");
            }
            returnTypeLetter = switch (type) {
                case "str" -> "Ljava/lang/String;";
                case "int" -> "I";
                case "bool" -> "Z";
                case "real" -> "F";
                case "str_array" -> "[Ljava/lang/String;";
                case "int_array" -> "[I";
                case "bool_array" -> "[Z";
                case "real_array" -> "[F";
                default -> "V";
            };

            container.visitField(ACC_PUBLIC | ACC_STATIC, id, returnTypeLetter, null, null).visitEnd();
        }

        // Constructor
        MethodVisitor methodInit = container.visitMethod(ACC_PUBLIC, "<init>", "(" + argLetter + ")V", null, null);
        methodInit.visitCode();
        methodInit.visitVarInsn(ALOAD, 0);
        methodInit.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        int idx = 0;
        for (ParamNode recordParameter : recordParameters) {
            methodInit.visitVarInsn(Opcodes.ALOAD, 0);

            returnTypeLetter = switch (recordParameter.getTypeStr()) {
                case "str" -> "Ljava/lang/String;";
                case "int" -> "I";
                case "bool" -> "Z";
                case "real" -> "F";
                case "str_array" -> "[Ljava/lang/String;";
                case "int_array" -> "[I";
                case "bool_array" -> "[Z";
                case "real_array" -> "[F";
                default -> "V";
            };
            methodInit.visitVarInsn(ALOAD, ++idx);
            methodInit.visitFieldInsn(Opcodes.PUTFIELD, name, recordParameter.getIdentifier(), returnTypeLetter);
        }
        methodInit.visitInsn(RETURN);
        methodInit.visitMaxs(-1, -1);
        methodInit.visitEnd();
        container.visitEnd();

        byte[] bytecode = container.toByteArray();
        try (FileOutputStream outputStream = new FileOutputStream(name + ".class")) {
            outputStream.write(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


}