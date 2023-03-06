/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package compiler;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;

import java.io.StringReader;

public class Compiler {
    public static void main(String[] args) {
        System.out.println("Hello from the compiler !");
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        for (Symbol symbol : lexer.symbols) {
            System.out.println(symbol.toString());
        }

    }
}
