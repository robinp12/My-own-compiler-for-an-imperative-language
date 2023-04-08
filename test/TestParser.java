import compiler.Lexer.Lexer;
import org.junit.Test;

import java.io.StringReader;
import java.text.ParseException;

import compiler.Parser.Parser;

public class TestParser {

    /*@Test
    public void testBasic() throws ParseException {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }*/
    @Test
    public void testBasicVal() throws ParseException {
        String input = "val a int = 3;";
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
        String input = "const a int = 3;";
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
        String input = "while va >= 3 { }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }

}
