package compiler.Semantic;

import compiler.Parser.AST.BlockNode;
import compiler.Parser.AST.MethodNode;
import compiler.Parser.AST.ParamNode;
import compiler.Parser.AST.TypeNode;

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

    public void checkTypes(MethodNode f, SymbolTable globalTable){
        SymbolTable localTable = new SymbolTable(globalTable);
        localTable.add(f.getParameters());
        checkTypes(f.getBody(),localTable);
    }
    public void checkTypes(BlockNode f, SymbolTable globalTable){

    }
}
