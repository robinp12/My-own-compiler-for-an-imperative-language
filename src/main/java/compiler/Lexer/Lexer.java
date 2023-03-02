package compiler.Lexer;
import java.io.Reader;
import java.util.Arrays;

public class Lexer {

    private int count;
    final static byte EOF = 0x1A;
    char[] array = new char[100];

    public Lexer(Reader input){
        try {
            input.read(array);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public int get_char(int i){
        return i < array.length ? this.array[i] : EOF;
    }

    public Symbol getNextSymbol() {
        while(true){
            switch (get_char(count)){
                case EOF:
                    System.out.println("EOF");
                    return null;
                case ' ':
                    // Case of space
                    break;
                case '\t':
                    // Case of tab
                    break;
                case '\n':
                    // Case of new line
                    break;
                case ';':
                    System.out.println("got an end line (;)");
                    break;
                case '/':
                    if(get_char(++count)=='/'){
                        // Case of comment
                        System.out.println("got a comment (//)");
                    }
                case '=':
                    System.out.println("Assignement '='");
                default:
                    // Case of identifier
                    // Case of keyword
                    // Case of value
                    int c = get_char(count);
                    if(c >= 'A' && c <= 'Z'
                        || c >= 'a' && c <= 'z'
                        || c == '_'){
                        System.out.println("letter : " + (char) c);
                    }
                    if(c >= '0' && c <= '9') {
                        System.out.println("number : " + (char) c);
                    }
            }
            count++;
        }
    }
}
