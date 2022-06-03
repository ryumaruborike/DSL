import java.util.*;

public class TokenType {
    static Map<String,String> regexp = new HashMap<String,String>();
    public TokenType(){
        regexp.put("WHILE","^WHILE$");
        regexp.put("COMPARISON_OP", "^[>|<|~]$");
        regexp.put("L_BC","^\\($");
        regexp.put("R_BC","^\\)$");
        regexp.put("IF_OPERATION", "^IF$");
        regexp.put("VAR", "^[a-z]+$");
        regexp.put("DIGIT", "^0|[1-9][0-9]*$");
        regexp.put("OPERATOR", "^[-|+|/|*|%]$");
        regexp.put("ASSIGNMENT_OPERATOR", "^=$");
        regexp.put("ENDLINE", "^\\;$");
        regexp.put("DO_WHILE", "^[D][O]$");
        regexp.put("DIV","^,$");
        regexp.put("PRINT","^PRINT$");
        regexp.put("FOR","^FOR$");
        regexp.put("ELSE", "^ELSE$");
        regexp.put("LL", "^LINKEDLIST$");
        regexp.put("POINT", "^\\.$");
        regexp.put("LLadd", "^ADD$");
        regexp.put("LLremove", "^REMOVE$");
        regexp.put("LLget", "^GET$");
        regexp.put("PRNTLIST", "^PRNTLIST$");
        regexp.put("LLsize", "^SIZE$");
    }
}