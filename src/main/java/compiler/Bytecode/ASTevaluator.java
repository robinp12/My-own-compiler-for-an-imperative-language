package compiler.Bytecode;
import compiler.Parser.AST.*;

public interface ASTevaluator {
    void visit(BinaryExpressionNode node) throws Exception;
}
