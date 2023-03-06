package compiler.Lexer;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Lexer {

    final static byte EOI = 0x1A;
    public final int[] input_string;
    private int i;

    public final Symbol[] symbols;


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
        input_string = str.codePoints().toArray();
        System.out.println(str);
        symbols = lex();

    }

    public Symbol[] lex(){
        ArrayList<Symbol> symbols = new ArrayList<>(input_string.length/10);
        i = 0;
        Symbol symbol;
        while ((symbol = getNextSymbol()) != null)
            symbols.add(symbol);

        return symbols.toArray(new Symbol[0]);
    }

    public int get_char(int i){
        return i < input_string.length ? this.input_string[i] : EOI;
    }

    public String get_literal(int i){
        String lit = new String();

        return lit;
    }
    
    public Symbol getNextSymbol() {
        int[] buf = new int[50];
        int start;
        while(true)
        {
            switch (get_char(i))
            {
                case ' ':
                case '\t':
                case '\n':
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
                        i++;
                        return new Symbol(SymbolKind.COMMENT);
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
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'b' :
                    if (get_char(i+1) == 'y'){
                        i+=2;
                        return new Symbol(SymbolKind.BY);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'i' :
                    if (get_char(i+1) == 'f'){
                        i+=2;
                        return new Symbol(SymbolKind.IF);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'o' :
                    if (get_char(i+1) == 'r'){
                        i+=2;
                        return new Symbol(SymbolKind.OR);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'a' :
                    if (get_char(i+1) == 'n' && get_char(i+2) == 'd'){
                        i+=3;
                        return new Symbol(SymbolKind.AND);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'f' :
                    if (get_char(i+1) == 'o' && get_char(i+2) == 'r'){
                        i+=3;
                        return new Symbol(SymbolKind.FOR);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'v' :
                    if (get_char(i+1) == 'a' && get_char(i+2) == 'r'){
                        i+=3;
                        return new Symbol(SymbolKind.VAR);
                    }
                    else if (get_char(i+1) == 'a' && get_char(i+2) == 'l'){
                        i+=3;
                        return new Symbol(SymbolKind.VAL);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'e' :
                    if (get_char(i+1) == 'l' && get_char(i+2) == 's' && get_char(i+3) == 'e'){
                        i+=4;
                        return new Symbol(SymbolKind.ELSE);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'p' :
                    if (get_char(i+1) == 'r' && get_char(i+2) == 'o' && get_char(i+3) == 'c'){
                        i+=4;
                        return new Symbol(SymbolKind.PROC);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'w' :
                    if (get_char(i+1) == 'h' && get_char(i+2) == 'i' && get_char(i+3) == 'l' && get_char(i+4) == 'e'){
                        i+=5;
                        return new Symbol(SymbolKind.WHILE);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'c' :
                    if (get_char(i+1) == 'o' && get_char(i+2) == 'n' && get_char(i+3) == 's' && get_char(i+4) == 't'){
                        i+=5;
                        return new Symbol(SymbolKind.CONST);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                case 'r' :
                    if (get_char(i+1) == 'e' && get_char(i+2) == 't' && get_char(i+3) == 'u' && get_char(i+4) == 'r' && get_char(i+5) == 'n'){
                        i+=6;
                        return new Symbol(SymbolKind.RETURN);
                    }
                    else if (get_char(i+1) == 'e' && get_char(i+2) == 'c' && get_char(i+3) == 'o' && get_char(i+4) == 'r' && get_char(i+5) == 'd'){
                        i+=6;
                        return new Symbol(SymbolKind.RECORD);
                    }
                    else
                        return new Symbol(SymbolKind.STRINGLITERAL,get_literal(i));

                default:
                    throw new IllegalArgumentException();

            }
        }
    }

}
