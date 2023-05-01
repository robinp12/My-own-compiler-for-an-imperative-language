package compiler.Lexer;

import java.io.IOException;
import java.io.Reader;

public class Lexer {

    final static byte EOI = 0x1A;
    public final int[] input_string;
    private int i;



    public Lexer(Reader input) {

        int nread = 0;
        char [] buffer = new char[100];
        String str = new String();
        while (nread != -1) {
            try {
                nread = input.read(buffer);
                if (nread > 0)
                    str += new String(buffer,0,nread);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        input_string = str.toLowerCase().codePoints().toArray();
        System.out.println(str);
    }


    public int get_char(int i){
        return i < input_string.length ? this.input_string[i] : EOI;
    }

    public String get_literal(){
        String lit = String.valueOf((char) get_char(i)); i++;
        while(i < input_string.length){
            char c = (char) get_char(i);
            switch (c){
                case 'a': case 'b': case 'c': case 'd':
                case 'e': case 'f': case 'g': case 'h':
                case 'i': case 'j': case 'k': case 'l':
                case 'm': case 'n': case 'o': case 'p':
                case 'q': case 'r': case 's': case 't':
                case 'u': case 'v': case 'w': case 'x':
                case 'y': case 'z': case '_': case '0':
                case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8':
                case '9':{ lit += String.valueOf(c); i++; break;}
                default:
                    return lit;
            }
        }
        return lit;
    }

    public String get_number(){
        String num = String.valueOf((char) get_char(i)); i++;
        while(i < input_string.length){
            char c = (char) get_char(i);
            switch (c){
                case '0': case '1': case '2': case '3':
                case '4': case '5': case '6': case '7':
                case '9': case '.': { num += String.valueOf(c); i++; break;}
                default:
                    return num;
            }
        }
        return num;
    }

    public String get_string(){
        String str = String.valueOf((char) get_char(i)); i++;
        while(i < input_string.length ){
            char c = (char) get_char(i);
            switch (c){
                case '"':
                    return str;
                default:
                    { str += String.valueOf(c); i++; break;}
            }
        }
        return str;
    }
    
    public Symbol getNextSymbol() {
        int c;
        while(i < input_string.length)
        {
            switch (get_char(i))
            {
                case ' ': case '\t': case '\n':
                    do {c = get_char(++i); }
                    while (c == ' ' || c == '\t' || c == '\n');
                    break;

                case EOI : return null;

                //delimiters
                case ',' : i++; return new Symbol(SymbolKind.COMA);
                case ';' : i++; return new Symbol(SymbolKind.SEMI);
                case '.' : i++; return new Symbol(SymbolKind.DOT);
                case ']' : i++; return new Symbol(SymbolKind.RBRACK);
                case '[' : i++; return new Symbol(SymbolKind.LBRACK);
                case '}' : i++; return new Symbol(SymbolKind.RBRACE);
                case '{' : i++; return new Symbol(SymbolKind.LBRACE);
                case ')' : i++; return new Symbol(SymbolKind.RPAR);
                case '(' : i++; return new Symbol(SymbolKind.LPAR);
                case '/' :
                    if (get_char(++i) == '/'){
                        // comments
                        StringBuilder str = new StringBuilder();
                        while (get_char(++i) != '\n' ){
                            if(get_char(i) == EOI) break;
                            str.append((char) get_char(i));
                        }
                        i++;
                        return new Symbol(SymbolKind.COMMENT,str.toString());
                    }
                    return new Symbol(SymbolKind.SLASH);

                //operators
                case '%' : i++; return new Symbol(SymbolKind.PERC);
                case '*' : i++; return new Symbol(SymbolKind.STAR);
                case '-' : i++; return new Symbol(SymbolKind.MINUS);
                case '+' : i++; return new Symbol(SymbolKind.PLUS);
                case '=' :
                    if (get_char(++i) == '='){
                        i++;
                        return new Symbol(SymbolKind.EQEQ);
                    }
                    return new Symbol(SymbolKind.EQUALS);
                case '<' :
                    if (get_char(++i) == '='){
                        i++;
                        return new Symbol(SymbolKind.LESSEQ);
                    }
                    else if (get_char(++i) == '>'){
                        i++;
                        return new Symbol(SymbolKind.DIFF);
                    }
                    return new Symbol(SymbolKind.LESS);
                case '>' :
                    if (get_char(++i) == '='){
                        i++;
                        return new Symbol(SymbolKind.MOREEQ);
                    }
                    return new Symbol(SymbolKind.MORE);

                //keywords
                case 't' :
                    if (get_char(i+1) == 'o'){
                        i+=2;
                        return new Symbol(SymbolKind.TO);
                    }
                    else if (get_char(i+1) == 'r' && get_char(i+2) == 'u' && get_char(i+3) == 'e'){
                        i+=4;
                        return new Symbol(SymbolKind.TRUE);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'b' :
                    if (get_char(i+1) == 'y'){
                        i+=2;
                        return new Symbol(SymbolKind.BY);
                    }
                    else if (get_char(i+1) == 'o' && get_char(i+2) == 'o' && get_char(i+3) == 'l' && get_char(i+4) == 'e' && get_char(i+5) == 'a' && get_char(i+6) == 'n'){
                        i+=7;
                        return new Symbol(SymbolKind.BOOL);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'i' :
                    if (get_char(i+1) == 'f'){
                        i+=2;
                        return new Symbol(SymbolKind.IF);
                    }
                    else if (get_char(i+1) == 'n' && get_char(i+2) == 't'){
                        i+=3;
                        return new Symbol(SymbolKind.INT);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'o' :
                    if (get_char(i+1) == 'r'){
                        i+=2;
                        return new Symbol(SymbolKind.OR);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'a' :
                    if (get_char(i+1) == 'n' && get_char(i+2) == 'd'){
                        i+=3;
                        return new Symbol(SymbolKind.AND);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'f' :
                    if (get_char(i+1) == 'o' && get_char(i+2) == 'r'){
                        i+=3;
                        return new Symbol(SymbolKind.FOR);
                    }
                    else if (get_char(i+1) == 'a' && get_char(i+2) == 'l' && get_char(i+3) == 's' && get_char(i+4) == 'e'){
                        i+=5;
                        return new Symbol(SymbolKind.FALSE);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'v' :
                    if (get_char(i) == 'v' && get_char(i+1) == 'a' && get_char(i+2) == 'r'){
                        i+=3;
                        return new Symbol(SymbolKind.VAR);
                    }
                    else if (get_char(i+1) == 'a' && get_char(i+2) == 'l'){
                        i+=3;
                        return new Symbol(SymbolKind.VAL);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'e' :
                    if (get_char(i+1) == 'l' && get_char(i+2) == 's' && get_char(i+3) == 'e'){
                        i+=4;
                        return new Symbol(SymbolKind.ELSE);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'p' :
                    if (get_char(i+1) == 'r' && get_char(i+2) == 'o' && get_char(i+3) == 'c'){
                        i+=4;
                        return new Symbol(SymbolKind.PROC);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'w' :
                    if (get_char(i+1) == 'h' && get_char(i+2) == 'i' && get_char(i+3) == 'l' && get_char(i+4) == 'e'){
                        i+=5;
                        return new Symbol(SymbolKind.WHILE);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'c' :
                    if (get_char(i+1) == 'o' && get_char(i+2) == 'n' && get_char(i+3) == 's' && get_char(i+4) == 't'){
                        i+=5;
                        return new Symbol(SymbolKind.CONST);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'r' :
                    if (get_char(i+1) == 'e' && get_char(i+2) == 'a' && get_char(i+3) == 'l'){
                        i+=4;
                        return new Symbol(SymbolKind.DOUBLE);
                    }
                    else if (get_char(i+1) == 'e' && get_char(i+2) == 't' && get_char(i+3) == 'u' && get_char(i+4) == 'r' && get_char(i+5) == 'n'){
                        i+=6;
                        return new Symbol(SymbolKind.RETURN);
                    }
                    else if (get_char(i+1) == 'e' && get_char(i+2) == 'c' && get_char(i+3) == 'o' && get_char(i+4) == 'r' && get_char(i+5) == 'd'){
                        i+=6;
                        return new Symbol(SymbolKind.RECORD);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 's':
                    if (get_char(i+1) == 't' && get_char(i+2) == 'r' && get_char(i+3) == 'i' && get_char(i+4) == 'n' && get_char(i+5) == 'g'){
                        i+=6;
                        return new Symbol(SymbolKind.STR);
                    }
                    else
                        return new Symbol(SymbolKind.LITERAL,get_literal());

                case 'g': case 'h': case 'j': case 'd':
                case 'k': case 'l': case 'm': case 'n':
                case 'q': case 'u': case 'x':
                case 'y': case 'z': case '_':
                    return new Symbol(SymbolKind.LITERAL,get_literal());

                case '0': case '1': case '2': case '3':
                case '4': case '5': case '6': case '7':
                case '8': case '9': return new Symbol(SymbolKind.NUM, get_number());

                case '"':
                    String str = get_string();
                    i++;
                    return new Symbol(SymbolKind.STRING, str);
                default:
                    System.out.println("Illegal argument : " + (char)get_char(i) );
                    throw new IllegalArgumentException();

            }
        }
        return null;
    }

}
