import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String[] exp;
    private ArrayList<Token> tokens = new ArrayList<>();
    private int len;

    public Lexer(String[] exp){
        this.exp = exp;
        this.len = exp.length;

        TokenType lex = new TokenType();

        String str_1 = "";
        for (int j = 0; j < exp.length;j++){
            for (int i = 0; i < exp[j].length(); i++) {
                if (exp[j].toCharArray()[i] == ' ') {
                    continue;
                }
                str_1 += exp[j].toCharArray()[i];
                String str_2 = " ";
                if (i < exp[j].length() - 1) {
                    str_2 = str_1 + exp[j].toCharArray()[i + 1];
                }
                for (String key : lex.regexp.keySet()) {
                    Pattern p = Pattern.compile(lex.regexp.get(key));
                    Matcher m_1 = p.matcher(str_1);
                    Matcher m_2 = p.matcher(str_2);
                    if (m_1.find() && !m_2.find()) {
                        tokens.add(new Token(key, str_1));
                        str_1 = "";
                    }
                }
            }
        }
    }

    public ArrayList<Token> get_tokens(){
        return tokens;
    }

    public int get_len(){
        return len;
    }
}
