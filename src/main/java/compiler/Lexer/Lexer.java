package compiler.Lexer;
import java.io.IOException;
import java.io.Reader;

public class Lexer {

    private Reader input;
    private StringBuilder stringBuilder;
    private int next;

    public Lexer(Reader input){
        this.input = input;
        this.stringBuilder = new StringBuilder();
        this.next = 0;

        int valChar;

        while (true) {
            try {
                if ((valChar = input.read()) == -1) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stringBuilder.append((char) valChar);
        }
    }

    public Symbol getNextSymbol() {
        char character = this.stringBuilder.charAt(next);
        this.next+=1;

        return new Symbol(character);
    }

}
