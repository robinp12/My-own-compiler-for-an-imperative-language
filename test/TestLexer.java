import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;
import org.junit.Test;

import java.io.StringReader;
import compiler.Lexer.Lexer;

import javax.management.InvalidApplicationException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class TestLexer {
    
    @Test
    public void testVar() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        for (Symbol symbol : lexer.symbols) {
            assertNotNull(symbol);
        }
    }
    @Test
    public void testComment() {
        String input = "//var a int = 0;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.symbols[0].kind,SymbolKind.COMMENT);
    }

    @Test
    public void testDoubleAssignment() {
        String input = "var x double = 2.00;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.symbols[2].kind,SymbolKind.DOUBLE);
        assertEquals(lexer.symbols[3].kind,SymbolKind.EQUALS);
    }

    @Test
    public void testIfElse() {
        String input = "if(10<=5) return 1 else return 0";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.symbols[0].kind,SymbolKind.IF);
        assertEquals(lexer.symbols[8].kind,SymbolKind.ELSE);
    }

    @Test
    public void testLitteral() {
        String input = "this is a test";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        for (Symbol symbol : lexer.symbols) {
            assertEquals(symbol.kind, SymbolKind.LITERAL);
        }
    }

    @Test
    public void testLessOrEqual() {
        String input = "var i = 5<=2";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertNotNull(lexer);
        assertEquals(lexer.symbols[4].kind,SymbolKind.LESSEQ);
    }

    @Test
    public void testEmptyInput() {
        String input = "    ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(0,lexer.symbols.length);
    }

    @Test
    public void testException() {
        String input = " # ";
        StringReader reader = new StringReader(input);
        assertThrows(IllegalArgumentException.class, () -> {
            Lexer lexer = new Lexer(reader);
        });
    }


}
