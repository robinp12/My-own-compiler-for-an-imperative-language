package compiler.Semantic;

import compiler.Parser.AST.*;

public interface Visitable {
    public void visit(Visitor visitor);

}