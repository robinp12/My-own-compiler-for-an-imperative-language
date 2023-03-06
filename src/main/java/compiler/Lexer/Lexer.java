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
    
    public Symbol getNextSymbol() {
        while(true)
        {
            int c;
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

                default:
                    
            }
        }
        return null;
    }

}
