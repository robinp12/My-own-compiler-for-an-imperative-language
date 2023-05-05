import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.*;
import compiler.Parser.Parser;
import compiler.Semantic.SemanticAnalyzer;
import compiler.Semantic.SymbolTable;
import org.junit.Test;

import java.io.StringReader;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

public class TestSemantic {
    
    @Test
    public void testBasicString() throws Exception {
        String input =  "val a string = \"coucou\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicInt() throws Exception {
        String input =  "val a int = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicReal() throws Exception {
        String input =  "val a real = 3.2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicBool() throws Exception {
        String input =  "val a boolean = True;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicVar() throws Exception {
        String input =  "var a real = 3.2; a = 4.8;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicMixedTypes() throws Exception {
        String input =  "var a real = 3.2; a = 4;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testBasicVal() throws Exception {
        String input =  "val a real = 3.2; a = 4.8;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testBasicDuplicatedVal() throws Exception {
        String input =  "val a string = \"coucou\"; val a string = \"cc\"; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testBasicDuplicatedVar() throws Exception {
        String input =  "var a int = 2; var a int = 4; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testBasicConst() throws Exception {
        String input =  "const a string = \"coucou\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicDuplicatedConst() throws Exception {
        String input =  "const a int = 2; const a int = 4; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});

    }

    @Test
    public void testMultipleDeclaration() throws Exception {
        String input =  "const a int = 2; const b real = 4.4; val c string = \"coucou\"; var d boolean = true; d = false; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicArrayIllegal() throws Exception {
        String input = "var c boolean[] = int[](10);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testBasicArray() throws Exception {
        String input = "var c int[] = int[](10);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testBasicArray2() throws Exception {
        String input = "var c int[] = int[](10); c[3] = 100;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }


}
