import compiler.Lexer.Lexer;
import compiler.Parser.AST.ExpressionNode;
import compiler.Parser.AST.ProgramNode;
import org.junit.Test;

import java.io.StringReader;
import java.text.ParseException;

import compiler.Parser.Parser;

public class TestParser {
        @Test
    public void testBasicVal() throws ParseException {
        String input = "val a boolean = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicVar() throws ParseException {
        String input = "var a int = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicConst() throws ParseException {
        String input = "const a int = \"test\"+1;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicProc() throws ParseException {
        String input = "proc square(v int) int {return 0+1;}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicIf() throws ParseException {
        String input = "if v == 10 { return true; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicElse() throws ParseException {
        String input = "if v == 10 { return true; } else { return false;}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicWhile() throws ParseException {
        // TODO CORRECT WHEN "value" is taken as "val" (also in other case)
        String input = "while va >= 3 { val x int; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicFor() throws ParseException {
        // TODO CORRECT WHEN "value" is taken as "val" (also in other case)
        String input = "for i=1 to 100 by 2 { val x int = 1; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
    @Test
    public void testBasicRecord() throws ParseException {
        String input = "record Point { x int; name string; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ExpressionNode x = parser.getAST();
        System.out.println(x);
    }

    @Test
    public void testBasicArray() throws ParseException {
        String input0 = "var c int[];";
        String input = "var c boolean[] = int[](10);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ExpressionNode x = parser.getAST();
        System.out.println(x);
    }

    @Test
    public void testBasicArrayAssignment() throws ParseException {
        String input = "c[2] = 1;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }
        
    @Test
    public void testBasicAssignment() throws ParseException {
        String input = "caov = \"true\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }

    @Test
    public void testBasicComplete() throws ParseException {
        String input = "var i int = 0;\n" +
                "var j int = 0;\n" +
                "var k int = 0;\n" +
                "//Comment\n" +
                "\n" +
                "for i=1+false to 10+true by 1 {\n" +
                "        for k=3+1+1 to 30 by 7.3 {\n" +
                "            val a string = 3;\n" +
                "        }\n" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        System.out.println(x);
    }

}
