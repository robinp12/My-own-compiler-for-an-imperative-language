package compiler.Bytecode;

import compiler.Lexer.Lexer;
import compiler.Parser.AST.*;
import compiler.Parser.Parser;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.StringReader;

public class BytecodeCompiler {
    private ClassWriter cw;
    public BytecodeCompiler(ProgramNode ast){

        this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC|Opcodes.ACC_STATIC ,"main","([Ljava/lang/String;)V",null,null);
        mv.visitCode();

        mv.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");
        mv.visitLdcInsn("hello");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream","println","(Ljava/lang/String;)V",false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();
        mv.visitMaxs(-1,-1);

        for (ExpressionNode expression : ast.getExpressions()) {
            if (expression instanceof ConstantDeclarationNode || expression instanceof ValDeclarationNode || expression instanceof VarDeclarationNode ||
                    expression instanceof RecordNode) {
                generateStatement((StatementNode) expression,null);
            }
        }
    }

    public void generateStatement(StatementNode stmt ,MethodVisitor mv) {

    }

}
