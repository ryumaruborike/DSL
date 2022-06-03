import java.util.regex.*;
import java.util.*;
public class Main {

    public static void main(String[] args) throws ParseExc {
        String[] exp = {
                "b = 5;",
                "a = 10;",
        "IF (a ~ 54) a = 33 ELSE a = 66;",
        "WHILE (a > 60) a = a - 1;",
        "FOR (i = 1 , i < 10, i = i + 1) b = b + 3;",
        "DO i = i * 10 WHILE (i < 100000);",
        "PRINT(b);",
        "PRINT(i);;",
        "LINKEDLIST u;",
        "u.ADD(7);",
        "u.ADD(9);",
        "u.ADD(56);",
        "u.REMOVE(1);",
        "u.REMOVE(1);",
        "PRNTLIST(u);",
        "u.ADD(12);",
        "u.ADD(890);",
        "u.GET(2);",
        "u.SIZE();",
        "PRNTLIST(u);",
        "LINKEDLIST p;",
        "p.ADD(7);",
        "p.ADD(8);",
        "p.ADD(7);",
        "p.ADD(8);",
        "p.REMOVE(3);",
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
