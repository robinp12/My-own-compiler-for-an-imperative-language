import compiler.Bytecode.BytecodeCompiler;
import compiler.Lexer.Lexer;
import compiler.Parser.AST.ProgramNode;
import compiler.Parser.Parser;
import compiler.Semantic.SemanticAnalyzer;
import org.junit.Test;

import java.io.StringReader;
import java.text.ParseException;

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
        String input =  "const a real = 1.1;"+
                "const aa int = 2; " +
                "const ab String = \"4\";"+
                "const xs boolean = true";
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
        String input =  "val a string = \"coucou\"; " +
                "val q string = \"cc\";" +
                "val as boolean = true;";
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
        String input = "var c real[] = real[](10);"+
                "var cc int[] = int[](20);";
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
                var ia int = 10;
                for i=1009999 to 1000 by 992 {
                        //
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
    public void testBasicReal() throws Exception {
        String input =  "var a real = 3.29;" +
                "val asss real = 1.129;"+
                "val x string = \"coucou\"; " +
                "var xx boolean = true;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }



    /*
        NOT WORKING YET
    */


    @Test
    public void testBasicInt() throws Exception {
        String input =  "var a int = 00;" +
                "a = 10;";
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
        String input =  "var a real = 3.2; val a int = 401;";
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
        String input =
                """
                proc add(x int, a int, aaa int) void {
                    return x + x;
                }
                add(1,2,33);
                """;
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
        String input =  "proc add() string {" +
                "return \"a\" + \"b\";" +
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
    public void testIfSimple() throws Exception {
        String input = """
                if true {
                    //...
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
    public void testIfBasic() throws Exception {
        String input = """
                const v int = 10;
                if v==10 {
                    //...
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
                if v==10 {
                    //...
                }else {
                    //...
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
    @Test
    public void testWhile() throws Exception {
        String input =
                "while 1 <= 4 { " +
                            "// ..." +
                        " }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testComplexWhile() throws Exception {
        String input =  "var i int = 0; while 8 >= 2 { i = i+1; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void test() throws Exception {
        String input =
        """
            var i int = 0; 
            while 8 >= 2 { 
                var ai int = 1; 
            }
        """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicProcssx() throws Exception {
        String input =
                """
                    proc square(v int) int {return 0+1;}
                    proc sqsuare(v int) int {return 0+1;}
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcs() throws Exception {
        String input =
                """
                    proc lens(v int) int {return 0+1;}
                    lens(1);
                    lens(2);
                    var i int = 0;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcss() throws Exception {
        String input =
                """
                    square = 1;
                    proc square(v int) int {return 0+1;}
                    var i int = 0;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        new SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

}
