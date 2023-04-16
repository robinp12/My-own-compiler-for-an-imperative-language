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
        Symbol symbol = lexer.getNextSymbol();
        assertNotNull(symbol);
        assertEquals(symbol.getKind(),SymbolKind.VAR);
        for (int i = 0; i < 3; i++) {
            symbol = lexer.getNextSymbol();
        }
        assertEquals(symbol.getKind(),SymbolKind.EQUALS);
    }
    @Test
    public void testComment() {
        String input = "//var a int = 0;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = lexer.getNextSymbol();
        do{
            assertEquals(symbol.getKind(),SymbolKind.COMMENT);
            symbol = lexer.getNextSymbol();
        } while (symbol != null);

    }

    @Test
    public void testDoubleAssignment() {
        String input = "var x double = 2.00;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = lexer.getNextSymbol();
        for (int i = 0; i < 2; i++) {
            symbol = lexer.getNextSymbol();
        }
        assertEquals(symbol.getKind(),SymbolKind.DOUBLE);
        symbol = lexer.getNextSymbol();
        assertEquals(symbol.getKind(),SymbolKind.EQUALS);
    }

    @Test
    public void testIfElse() {
        String input = "if(10<=5) return 1 else return 0";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = lexer.getNextSymbol();
        assertEquals(symbol.getKind(),SymbolKind.IF);
        for (int i = 0; i < 8; i++) {
            symbol = lexer.getNextSymbol();
        }
        assertEquals(symbol.getKind(),SymbolKind.ELSE);
    }

    @Test
    public void testLitteral() {
        String input = "this is a test";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = lexer.getNextSymbol();
        do{
            assertEquals(symbol.getKind(), SymbolKind.LITERAL);
            symbol = lexer.getNextSymbol();
        } while (symbol != null);
    }

    @Test
    public void testLessOrEqual() {
        String input = "var i = 5<=2";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertNotNull(lexer);
        Symbol symbol = lexer.getNextSymbol();
        for (int i = 0; i < 4; i++) {
            symbol = lexer.getNextSymbol();
        }
        assertEquals(symbol.getKind(),SymbolKind.LESSEQ);

    }

    @Test
    public void testEmptyInput() {
        String input = "    ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = lexer.getNextSymbol();
        assertNull(symbol);
    }

    @Test
    public void testException() {
        String input = " # ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertThrows(IllegalArgumentException.class, () -> {
            lexer.getNextSymbol();
        });
    }


}
