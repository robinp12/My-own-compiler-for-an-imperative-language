import compiler.Bytecode.BytecodeCompiler;
import compiler.Lexer.Lexer;
import compiler.Parser.AST.ProgramNode;
import compiler.Parser.Parser;
import compiler.Semantic.SemanticAnalyzer;
import org.junit.Test;

import java.io.StringReader;

public class TestByteCode {

    @Test
    public void testBasicProc() throws Exception {
        String input =  """
                proc add() int {
                    return 10+10;
                }
                add();
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcCall() throws Exception {
        String input =
                """
                    proc square() int {return 1;}
                    proc squared() int {return 0;}
                    square();
                    squared();
                    squared();
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcString() throws Exception {
        String input =  "proc add() string {" +
                "var a int = 1/1;"+
                "var b int = 2%2;"+
                "return a;" +
                "}" +
                "add()";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testComplexProcs() throws Exception {
        String input =
                """
                    var square int = 1;
                    proc square(v int) int {
                        var b int = 111;
                        var ddd int = 22;
                        return 0+1;
                    }
                    var i int = 2;
                    square(222);
                    var dadd int = 3;
                    proc newfun(v int) int {
                        var b int = 111;
                        var ddd int = 22;
                        return 0+1;
                    }
                    newfun(100);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
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
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
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
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicArrayReal() throws Exception {
        String input =
            """
                var c real[] = real[](10);
                var cc int[] = int[](20);
            """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testForLoop() throws Exception {
        String input = """
                var i int;
                var ia int = 1337;
                for i=1009999 to 1000 by 992 {
                        write("print");
                        ia = 999;
                        var a string = "test";
                    }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
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
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicVar() throws Exception {
        String input = """ 
                var a int;
                var b real;
                var c boolean;
                var d string;
                a = 4;
                b = 1.1;
                c = true;
                d = \"yooo\";
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicInt() throws Exception {
        String input =  "var a int;" +
                "a = 102200;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testMultipleDeclaration() throws Exception {
        String input =  "const a int = 2; " +
                "const b real = 4.4; " +
                "val c string = \"coucou\"; " +
                "var d boolean = true; " +
                "d = false; ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testIfSimple() throws Exception {
        String input = """
                var a int = 10;
                if true {
                    a = 10;
                }
                else{
                    a = 100;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testIfSimple2() throws Exception {
        String input = """
                var a int = 10;
                if true {
                    a = 10;
                }else{
                    var b int;
                }
                if true {
                    a = 100;
                }else{
                    var b int;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testArrayReassignment() throws Exception {
        String input = "var ac boolean[] = boolean[](10); " +
                "var c string[] = string[](10); " +
                "var rc real[] = real[](10); " +
                "var drc int[] = int[](10); " +
                "ac[3] = true;" +
                "c[3] = \"sc\";" +
                "rc[3] = 1.1;" +
                "drc[3] = 1;" +
                "";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBasicProcParam() throws Exception {
        String input =
                """
                    proc add(x boolean, a int, b string) int {
                        return a;
                    }
                    add(true,1,"oui");
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBuiltIn() throws Exception {
        String input = """
                proc print() int {
                    writeln("rovib");
                    return 1;
                }
                print();
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testBuiltInVarAssigment() throws Exception {
        String input = """
                var a int = chr(65);
                var leng int = len("test");
                not(true);
                var f int = floor(5.5);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);;
        bc.getRender();
    }

    @Test
    public void testRecordWithArrayAssign() throws Exception {
        String input = """
                record Pointe {
                    x String[];
                    y int[];
                    xx boolean;
                    yy real;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcs() throws Exception {
        String input =
                """
                    proc lens(v int, a int) int {return v+v;}
                    proc lens1(v int) int {return v+v;}

                    lens(20000,11);
                    lens1(111);
                    var i int = 0;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testProcIllegalType2() throws Exception {
        String input =
                """
                proc add(x int, a int, aaa int) int {
                    return x + x;
                }
                add(1,2,33);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    /*
        NOT WORKING YET
    */

    @Test
    public void testArrayReassgnment() throws Exception {
        String input = "var ac boolean[] = boolean[](10); " +
                "var c string[] = string[](10); " +
                "var rc real[] = real[](10); " +
                "var drc int[] = int[](10); " +
                "ac[3] = true;" +
                "c[3] = \"sc\";" +
                "rc[3] = 1.1;" +
                "drc[3] = 1;" +
                "";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testRecordAssign2() throws Exception {
        String input = """
                record Point {
                    x int;
                    y int;
                }
                                
                record Person {
                    name string;
                    history int[];
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcssx2() throws Exception {
        String input =
                """
                    proc squared(v int, x int) int {return v%x;}
                    proc square(v int) int {return v;}
                    squared(10,10);
                    square(10);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testIfSameVar2() throws Exception {
        String input = """
                var i int = 1;
                if true {
                    var i int = 1;
                }else{
                    var i int = 1;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    /*
        NOT WORKING YET
    */

    @Test
    public void testArrayReassignmentString() throws Exception {
        String input = "var c string[] = string[](10); " +
                "c[3] = \"sc\";" +
                "";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
/*
    @Test
    public void testBinaryOperationWithRegisters() throws Exception {
        String input = """
                var i int = 10;
                var ii int = 10;
                var sum int = i + ii;
                var a int = i * 20;
                var as int = i * ii;
                var aa int = i * 20 + ii;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
*/
    @Test
    public void testDeclarationBool() throws Exception {
        String input = "var x boolean = (1+1==2); " +
                "var xs boolean = (1>=2);" +
                "val x10 boolean = (1*2==2);" +
                "val x0 boolean = (1*2!=2);" +
                "val x1 boolean = (2.0==2.0);" +
                "val x12 boolean = (2.0<=20.0);" +
                "";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }


    @Test
    public void testProc() throws Exception {
        String input =  "proc double(x int, aaaa int) int {" +
                "var r int = 10 + 1000;"+
                "return x;" +
                "}" +
                "double(1,2);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testProcIllegalType() throws Exception {
        String input =
                """
                proc add(x int, a int, aaa int) int {
                    return x + x;
                }
                add(1,2,33);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testprocexemple1() throws Exception {
        String input =
                """
                proc square(v int) int {
                    return v*v;
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testprocexemple2() throws Exception {
        String input =
                """
                var c int[] = int[](5);  // new array of length 5
                        """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testprocexemple3() throws Exception {
        String input =
                """
                 const i int = 3;
                 const j real = 3.2;
                 const message string = "Hello";
                 const isEmpty boolean = true;
                        """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
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
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testRecordComplexAssign() throws Exception {
        String input = """
                record Point {
                    x int;
                    y int;
                }
                                
                record Person {
                    name string;
                    age int;
                    kids int;
                    married boolean;
                    history int[];
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testIfSameVar() throws Exception {
        String input = """
                var i int = 1;
                if true {
                    var i int = 1;
                }else{
                    var i int = 1;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    /*
    @Test
    public void testIfBasic() throws Exception {
        String input = """
                proc res() void {
                    const v int = 10;
                    if v==10 {
                        //...
                    }
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
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
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testWhile() throws Exception {
        String input = """
                proc add() void{
                    while 1 <= 4 {
                        //...
                    }
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
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
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
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
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
*/
    @Test
    public void testBasicProcssx() throws Exception {
        String input =
                """
                    proc squared(v int, x int) int {return v%x;}
                    proc square(v int) int {return v;}
                    squared(10,10);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testBasicProcs3() throws Exception {
        String input =
                """
                    proc lens(v int, a int) int {return v+v;}
                    proc lens1(a int) int {return v+v;}

                    lens(20000,11);
                    lens1(111);
                    var i int = 0;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testForLoopinProc() throws Exception {
        String input = """
                var i int;
                var ia int = 10;
                proc loop() void {
                    for i=999 to 1000 by 1 {
                        //...
                    }
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    @Test
    public void testIfSimples() throws Exception {
        String input = """
                var a int = 10;
                if true {
                    var as int = 10;
                }
                else{
                    var as int = 10;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }
    /*
    @Test
    public void testBinaryOperationWithRegister() throws Exception {
        String input = """
                var i int = 10;
                var ii int = 10;
                var sum int = i + ii;
                var a int = i * 20.1;
                var as int = i * "sttttt";
                var aa int = i * 20;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }

    @Test
    public void testMain() throws Exception {
        String input = """
            proc main() void {
                var value int = 3;
                var i int;
                for i=1 to 100 by 2 {
                    while value!=3 {
                        // ....
                    }
                }
                i = (i+2)*2;
            }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sa = new SemanticAnalyzer();
        x = sa.SemanticAnalyzer(x);
        BytecodeCompiler bc = new BytecodeCompiler(x);
        bc.getRender();
    }*/
}
