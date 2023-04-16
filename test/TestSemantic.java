import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.ExpressionNode;
import compiler.Parser.AST.ProgramNode;
import compiler.Parser.AST.StatementNode;
import compiler.Parser.Parser;
import compiler.Semantic.SymbolTable;
import org.junit.Test;

import java.io.StringReader;
import java.text.ParseException;

import static org.junit.Assert.*;

public class TestSemantic {
    
    @Test
    public void testBasic() throws ParseException {
        String input = "var c boolean[] = int[](10);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ExpressionNode x = parser.getAST();
        System.out.println(x);
        SymbolTable st = new SymbolTable(null);
    }
}
