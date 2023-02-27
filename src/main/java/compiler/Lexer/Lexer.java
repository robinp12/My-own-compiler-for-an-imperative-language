package compiler.Lexer;
import java.io.Reader;
import java.util.Arrays;

public class Lexer {

    private int count;
    final static byte EOI = 0x1A;
    char[] array = new char[100];

    public Lexer(Reader input){
        try {
            input.read(array);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public int get_char(int i){
        return i < array.length ? this.array[i] : EOI;
    }

    public Symbol getNextSymbol() {
        while(true){
            System.out.println((char) get_char(count));
            switch (get_char(count)){
                case ' ':
                    break;
                case '\t':
                case '\n':
                case '/':
                    if(get_char(++count)=='/'){
                        // Case of comment
                        System.out.println("got a comment (//)");
                    }
                case ';':
                    System.out.println("got an end line (;)");
                case EOI:
                    System.out.println("EOF");
                    return null;
            }
            count++;
        }
    }
}
