package compiler.Semantic;

import compiler.Parser.AST.*;

public interface Visitor {
    public <T> T visit(ProgramNode node);

    public <T> T visit(ParamNode node);

    public <T> T visit(AssignmentNode node);

}