import java.util.regex.*;
import java.util.*;
public class Main {

    public static void main(String[] args) throws ParseExc {
        String[] exp = {
                "b = 5;",
                "a = 10;",
        "IF (a ~ 54) a = 25 ELSE a = 52;",
        "WHILE (a > 59) a = a - 1;",
        "PRINT(a);",
        "FOR (i = 1 , i < 10, i = i + 1) b = b + 3;",
        "DO i = i * 10 WHILE (i < 100);",
        "PRINT(b);",
        "PRINT(i);;",
        "LINKEDLIST f;",
        "f.ADD(7);",
        "f.ADD(9);",
        "f.ADD(56);",
        "f.REMOVE(1);",
        "f.REMOVE(1);",
        "PRNTLIST(f);",
        "f.ADD(12);",
        "f.ADD(80);",
        "f.GET(2);",
        "f.SIZE();",
        "PRNTLIST(f);",
        "LINKEDLIST p;",
        "p.ADD(1);",
        "p.ADD(2);",
        "p.ADD(3);",
        "p.ADD(4);",
        "p.REMOVE(2);",
        "PRNTLIST(p);",
        "p.SIZE();"};

        Lexer lex = new Lexer(exp);

        Parser par = new Parser(lex.get_tokens(), lex.get_len());
        try {
            par.lang();
        }
        catch (IndexOutOfBoundsException ignored){};
        Inter inter = new Inter(lex.get_tokens());
    }
}
