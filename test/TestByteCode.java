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
    public void testBasicProc() throws Exception {
        String input =  "proc add() int {" +
                "return 10 + 1;" +
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
    public void testBasicProcString() throws Exception {
        String input =  "proc add() string {" +
                "return \"Hola\";" +
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
    public void testBasicConst() throws Exception {
        String input =  "const a real = 1.1;";
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
        String input =  "const a int = 2; const ab String = \"4\"; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
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
    public void testBasicArrayReal() throws Exception {
        String input = "var c real[] = real[](10);";
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
        String input = "var c int[] = int[](20);";
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
        String input =  "var a int ;";
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
        String input =  "val a boolean = false;";
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
    public void testBasicVar2() throws Exception {
        String input =  "var a int = 3; " +
                "       var aq boolean = true;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcParam() throws Exception {
        String input =  "proc add(x boolean, a int, b string) int {" +
                "return 10 + 1;" +
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
    public void testBasicProcParamVar() throws Exception {
        String input =  "proc add(x boolean, a int, b string) int {" +
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
    public void testBasicMixedTypes() throws Exception {
        String input =  "var a real = 3.2; val a int = 4;";
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
        String input =  "val a real = 3.2; var ad real = 4.8;";
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
        String input =  "val a string = \"coucou\"; val q string = \"cc\"; ";
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
        String input =  "var a int = 2; var ab int = 4; ";
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
        String input =  "const a int = 2; " +
                "const b real = 4.4; " +
                "val c string = \"coucou\"; " +
                "var d boolean = true; d = false; ";
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
                "const x boolean = true";
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
    public void testAssignmentVal() throws Exception {
        String input = "val x string = \"coucou\"; " +
                "var xx boolean = true ";
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
        String input =  "proc double(x int) int {" +
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
                var i int = 0;
                for i=100 to 1000 by 2 {
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
                if true {
                    return true;
                }else {
                    return false;
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
