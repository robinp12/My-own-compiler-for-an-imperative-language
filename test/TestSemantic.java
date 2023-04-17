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
    public void testBasic() throws ParseException {
        String input =  "for i=1+false to 10+true by 1 {\n" +
                "        for k=3+1+1 to 30 by 7.3 {\n" +
                "            val a string = 3;\n" +
                "        }\n" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        ProgramNode x = parser.getAST();
        SemanticAnalyzer sem = new SemanticAnalyzer();
        for (ExpressionNode expression : x.getExpressions()) {
            sem.visit(expression);
        }
    }
}
