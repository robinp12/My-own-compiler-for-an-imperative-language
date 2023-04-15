package compiler.Semantic;

import compiler.Parser.AST.*;

public interface TypeVisitor {
    public <T> T visit(ProgramNode node);

    public <T> T visit(ParamNode node);

    public <T> T visit(AssignmentNode node);

}