import compiler.Bytecode.BytecodeCompiler;
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
    public void testBasicWhile() throws Exception {
        String input =  "while 8 >= 3 { val x int; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testComplexWhile() throws Exception {
        String input =  "var i int = 0; while 8 >= i { i = i+1; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testComplexWhileIllegal() throws Exception {
        String input =  "var i boolean = true; while 8 >= i { i = i+1; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
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

    @Test
    public void testAssignmentConstIllegal() throws Exception {
        String input =  "const x int; " +
                "x = 10";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testAssignmentVarIllegal() throws Exception {
        String input =  "var x int; " +
                "x = 10.5";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testAssignmentValIllegal() throws Exception {
        String input = "val x string = \"coucou\"; " +
                "x = true ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testDeclarationBool() throws Exception {
        String input = "var x boolean = (1==1); ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        System.out.println(x);
        new SemanticAnalyzer(x);
    }

    @Test
    public void testCheckBinary() throws Exception {
        String input = "var x int = (1+1); ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        System.out.println(x);
        new SemanticAnalyzer(x);
    }

    @Test
    public void testCheckIllegalBinary() throws Exception {
        String input = "var x int = 1+true; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        System.out.println(x);
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testAssignmentBool1() throws Exception {
        String input = "var x boolean = true;" +
                "x = (1+1==3); ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        System.out.println(x);
        new SemanticAnalyzer(x);
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
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
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
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
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
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
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
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
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
        System.out.println(x);
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testRecord() throws Exception {
        String input = """
                record point {
                    x int;
                    y int;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testRecordUsage() throws Exception {
        String input = """
                record point {
                    x int;
                    y int;
                }
                point.x = 1;
                point.y = 4;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
    }

    @Test
    public void testRecordUsageIllegal1() throws Exception {
        String input = """
                record point {
                    x int;
                    y int;
                }
                point.x = 1;
                point.z = 4;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testRecordUsageIllegal2() throws Exception {
        String input = """
                record point {
                    x int;
                    y int;
                }
                point.x = 1;
                point.y = true;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testRecordIllegal() throws Exception {
        String input = """
                record point {
                    x int;
                    x int;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testIf() throws ParseException {
        String input = """
                const v int = 10;
                if v == 10 {
                    return true;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }

    @Test
    public void testIfIllegalType() throws ParseException {
        String input = """
                const v int = 3;
                if v == "test" {
                    return true;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testIfIllegalNoDecl() throws ParseException {
        String input = """
                if v == 10 {
                    return true;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

    @Test
    public void testBuiltIn() throws ParseException {
        String input = """
                    proc readInt() int {
                        return 10 + 1;
                    }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        assertThrows(Exception.class, () -> {new SemanticAnalyzer(x);});
    }

}
