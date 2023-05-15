import compiler.Bytecode.BytecodeCompiler;
import compiler.Lexer.Lexer;
import compiler.Parser.AST.ProgramNode;
import compiler.Parser.Parser;
import compiler.Semantic.SemanticAnalyzer;
import org.junit.Test;

import java.io.StringReader;
import java.text.ParseException;
import java.util.Arrays;

import static org.junit.Assert.assertThrows;

public class TestByteCode {
    
    @Test
    public void testBasicString() throws Exception {
        String input =  "val a string = \"coucou\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicInt() throws Exception {
        String input =  "val a int = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicReal() throws Exception {
        String input =  "val a real = 3.2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicBool() throws Exception {
        String input =  "val a boolean = True;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicVar() throws Exception {
        String input =  "var a real = 3.2; a = 4.8;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicMixedTypes() throws Exception {
        String input =  "var a real = 3.2; a = 4;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicVal() throws Exception {
        String input =  "val a real = 3.2; a = 4.8;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicDuplicatedVal() throws Exception {
        String input =  "val a string = \"coucou\"; val a string = \"cc\"; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicDuplicatedVar() throws Exception {
        String input =  "var a int = 2; var a int = 4; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicConst() throws Exception {
        String input =  "const a string = \"coucou\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicDuplicatedConst() throws Exception {
        String input =  "const a int = 2; const a int = 4; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();

    }

    @Test
    public void testMultipleDeclaration() throws Exception {
        String input =  "const a int = 2; const b real = 4.4; val c string = \"coucou\"; var d boolean = true; d = false; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicArrayIllegal() throws Exception {
        String input = "var c boolean[] = int[](10);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicArray() throws Exception {
        String input = "var c int[] = int[](10);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicArray2() throws Exception {
        String input = "var c int[] = int[](10); c[3] = 100;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testAssignmentConstIllegal() throws Exception {
        String input =  "const x int; " +
                "x = 10";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testAssignmentVarIllegal() throws Exception {
        String input =  "var x int; " +
                "x = 10.5";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testAssignmentValIllegal() throws Exception {
        String input = "val x string = \"coucou\"; " +
                "x = true ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testDeclarationBool() throws Exception {
        String input = "var x boolean = (1==1); ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testAssignmentBool1() throws Exception {
        String input = "var x boolean = true;" +
                "x = (1+1==3); ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testProc() throws Exception {
        String input =  "proc add(x int) int {" +
                "return x + x;" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testProcIllegalType() throws Exception {
        String input =  "proc add(x int) void {" +
                "return x + x;" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testProcIllegalType1() throws Exception {
        String input =  "proc add(x int) string {" +
                "return x + x;" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testProcIllegalNoReturn() throws Exception {
        String input =  "proc add(x int) boolean {" +
                "x + x;" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testProcNoDeclVarIllegal() throws Exception {
        String input =  "proc add() int {" +
                "return x + x;" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }


    @Test
    public void testForLoop() throws Exception {
        String input = """
                var i int;
                for i=1 to 100 by 2 {
                        // ...
                    }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testForLoopNoDeclIllegal() throws Exception {
        String input = """
                for i=1 to 100 by 2 {
                        // ...
                    }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testForLoopIllegal() throws Exception {
        String input = """
                var i int;
                for i=1 to "100" by 2 {
                        // ...
                    }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testRecord() throws Exception {
        String input = """
                record Point {
                    x int;
                    y int;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testRecordIllegal() throws Exception {
        String input = """
                record Point {
                    x Point;
                    y Point;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testIf() throws Exception {
        String input = """
                const v int = 10;
                if v == 10 {
                    return true;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testIfIllegalType() throws Exception {
        String input = """
                const v int = 3;
                if v == "test" {
                    return true;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testIfIllegalNoDecl() throws Exception {
        String input = """
                if v == 10 {
                    return true;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

}
