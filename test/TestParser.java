import compiler.Lexer.Lexer;
import org.junit.Test;

import java.io.StringReader;
import compiler.Parser.Parser;

public class TestParser {

    @Test
    public void testBasic() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.getAST();
    }

}
