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
            String name = paramNode.getIdentifier();
            TypeNode type = paramNode.getType();
            entries.put(name,type);
        }
    }
    public TypeNode getTypeOfExpression(ExpressionNode e){
        return new TypeNode(null,null);
    }
    public void checkTypes(ProgramNode p, SymbolTable globalTable){
        for (ExpressionNode expression : p.getExpressions()) {
        }
    }
    public void checkTypes(ExpressionNode e, SymbolTable globalTable){
    }

    public void checkTypes(MethodNode m, SymbolTable globalTable){
        SymbolTable localTable = new SymbolTable(globalTable);
        localTable.add(m.getParameters());
        checkTypes(m.getBody(),localTable);
    }
    public void checkTypes(BlockNode b, SymbolTable globalTable){
        checkTypes(b.getStatements());
    }

    public void checkTypes(StatementNode statements) {
        for (ExpressionNode statement : statements.getStatements()) {

            System.out.println(statement);
        }
    }
    public void checkTypes(AssignmentArrayNode array){

    }
}
