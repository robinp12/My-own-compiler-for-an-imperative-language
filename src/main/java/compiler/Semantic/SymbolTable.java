package compiler.Semantic;

import compiler.Parser.AST.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    SymbolTable previousTable;
    HashMap<String, TypeNode> entries;
    public SymbolTable(SymbolTable prev){
        this.previousTable = prev;
    }
    public void add(ArrayList<ParamNode> n){
        for (ParamNode paramNode : n) {
            String name = paramNode.getName();
            TypeNode type = paramNode.getType();
            entries.put(name,type);
        }
    }
    public TypeNode getTypeOfExpression(ExpressionNode e){
        return new TypeNode(null);
    }

    public void checkTypes(MethodNode m, SymbolTable globalTable){
        SymbolTable localTable = new SymbolTable(globalTable);
        localTable.add(m.getParameters());
        checkTypes(m.getBody(),localTable);
    }
    public void checkTypes(BlockNode b, SymbolTable globalTable){
        checkTypes(b.getStatements());
    }

    public void checkTypes(ArrayList<StatementNode> statements) {
        for (StatementNode statement : statements) {
        }
    }
    public void checkTypes(ArrayNode array){

    }
}
