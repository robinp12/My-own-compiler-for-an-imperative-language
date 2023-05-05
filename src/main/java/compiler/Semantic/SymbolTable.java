package compiler.Semantic;

import compiler.Parser.AST.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class   SymbolTable {
    //SymbolTable previousTable;
    private static Map<String, TypeNode> mutablesymbolTable;
    private static Map<String, TypeNode> immutablesymbolTable;

    public SymbolTable(){
        this.mutablesymbolTable = new HashMap<>();
        this.immutablesymbolTable = new HashMap<>();
    }

    public static void insertmut(String name, TypeNode type) {
        mutablesymbolTable.put(name, type);
    }
    public static void insertimmut(String name, TypeNode type) {
        immutablesymbolTable.put(name, type);
    }

    public static TypeNode lookupmut(String name) {
        return mutablesymbolTable.get(name);}
    public static TypeNode lookupimmut(String name) {
        return immutablesymbolTable.get(name);}

    public static boolean containmut(String name) {
        return mutablesymbolTable.containsKey(name);
    }
    public static boolean containimmut(String name) {
        return immutablesymbolTable.containsKey(name);
    }

/*
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

 */
}
