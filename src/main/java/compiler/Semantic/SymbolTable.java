package compiler.Semantic;

import compiler.Parser.AST.*;

import java.util.*;

public class   SymbolTable {
    //SymbolTable previousTable;
    private static Map<String, TypeNode> mutablesymbolTable;
    private static Map<String, TypeNode> immutablesymbolTable;
    private static Map<String, List<AbstractMap.SimpleEntry<String, String>>> recordsymbolTable;
    private static Map<String, AbstractMap.SimpleEntry<String, Integer>> arraysymbolTable;

    public SymbolTable(){
        this.mutablesymbolTable = new HashMap<>();
        this.immutablesymbolTable = new HashMap<>();
        this.recordsymbolTable = new HashMap<>();
        this.arraysymbolTable = new HashMap<>();

    }

    public static void insertmut(String name, TypeNode type) {
        mutablesymbolTable.put(name, type);
    }
    public static void insertimmut(String name, TypeNode type) {
        immutablesymbolTable.put(name, type);
    }
    public static void insertrecord(String name, List<AbstractMap.SimpleEntry<String, String>> fields ){
        recordsymbolTable.put(name, fields);
    }
    public static void insertarray(String name, AbstractMap.SimpleEntry<String, Integer> fields ){
        arraysymbolTable.put(name, fields);
    }

    public static TypeNode lookupmut(String name) {
        return mutablesymbolTable.get(name);}
    public static TypeNode lookupimmut(String name) {
        return immutablesymbolTable.get(name);}
    public static List<AbstractMap.SimpleEntry<String, String>> lookuprecord(String name){
        return recordsymbolTable.get(name);
    }
    public static AbstractMap.SimpleEntry<String, Integer> lookuparray(String name){
        return arraysymbolTable.get(name);
    }

    public static boolean containmut(String name) {
        return mutablesymbolTable.containsKey(name);
    }
    public static boolean containimmut(String name) {
        return immutablesymbolTable.containsKey(name);
    }
    public static boolean containrecord(String name){
        return recordsymbolTable.containsKey(name);
    }
    public static boolean containarray(String name){
        return arraysymbolTable.containsKey(name);
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
