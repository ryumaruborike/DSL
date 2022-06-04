import java.util.ArrayList;
import java.util.Objects;

public class Parser {

    private final ArrayList<Token> tokens;
    private final int len;

    private int iterator;
    private int curLine;
    private Token curToken;

    public boolean correctCode;

    public Parser(ArrayList<Token> tokens, int len) {
        this.tokens = tokens;
        this.len = len;
        curLine = 0;
        iterator = 0;
        curToken = tokens.get(iterator);
        correctCode = true;
    }

    public void TERMINAL(String tokenType) throws ParseExc {
        if (!Objects.equals(curToken.getType(), tokenType)) {
            correctCode = false;
            throw new ParseExc(curLine, iterator, curToken, tokenType);
        }
    }

    public void check(String tokenType) {
        try {
            TERMINAL(tokenType);
        } catch (ParseExc e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);
    }

    public boolean body_condition() {
        return switch (curToken.getType()) {
            case "VAR", "IF_OPERATION", "FOR", "WHILE", "DO_WHILE", "PRINT" -> true;
            default -> false;
        };
    }

    public boolean body_condition_do_while() {
        return switch (curToken.getType()) {
            case "VAR", "IF_OPERATION", "FOR", "DO_WHILE", "PRINT" -> true;
            default -> false;
        };
    }

    public void lang() throws ParseExc {
        for (int i = 0; i < len; i++) {
            curLine++;
            expr();
        }
    }

    public void expr() {
        body();
        check("ENDLINE");
    }

    public void body() {
        switch (curToken.getType()) {
            case "VAR" -> var();
            case "IF_OPERATION" -> if_op();
            case "WHILE" -> while_op();
            case "DO_WHILE" -> do_while_op();
            case "FOR" -> for_op();
            case "PRINT" -> print();
            case "LL" -> linked_list();
            case "PRNTLIST" -> linked_list_print();
            default -> check("VAR");
        }
    }

    public void expr_value() {
        switch (curToken.getType()) {
            case "VAR", "DIGIT" -> value();
            case "L_BC" -> infinity();
            default -> check("VAR");
        }
        while ("OPERATOR".equals(curToken.getType())) {
            check("OPERATOR");
            value();
        }
    }

    public void value() {
        switch (curToken.getType()) {
            case "DIGIT" -> check("DIGIT");
            case "L_BC" -> infinity();
            default -> check("VAR");
        }
    }

    public void infinity() {
        check("L_BC");
        expr_value();
        check("R_BC");
    }

    public void condition() {
        check("VAR");
        check("COMPARISON_OP");
        expr_value();
    }

    public void condition_in_br() {
        check("L_BC");
        condition();
        check("R_BC");
    }

    public void if_op() {
        check("IF_OPERATION");
        condition_in_br();
        do {
            body();
        } while (body_condition());
        if ("ELSE".equals(curToken.getType())) {
            else_op();
        }
    }

    public void else_op() {
        check("ELSE");
        do {
            expr();
        } while (body_condition());
    }

    public void while_op() {
        check("WHILE");
        condition_in_br();
        do {
            body();
        } while (body_condition());
    }

    public void do_while_op() {
        check("DO_WHILE");
        do {
            body();
        } while (body_condition_do_while());
        check("WHILE");
        condition_in_br();
    }

    public void for_op() {
        check("FOR");
        check("L_BC");
        check("VAR");
        assign();
        check("DIV");
        condition();
        check("DIV");
        check("VAR");
        assign();
        check("R_BC");
        do {
            body();
        } while (body_condition());
    }

    public void assign() {
        //terminalCheck("VAR");
        check("ASSIGNMENT_OPERATOR");
        expr_value();
    }

    public void var() {
        check("VAR");
        if("POINT".equals(curToken.getType())){
            ll_operate();
        }
        else if("ASSIGNMENT_OPERATOR".equals(curToken.getType())){
            expr_assign();
        }
    }

    public void ll_operate(){
        //terminalCheck("VAR");
        check("POINT");
        if("LLadd".equals(curToken.getType())){
            check("LLadd");
            if("L_BC".equals(curToken.getType())){
                check("L_BC");
                if("VAR".equals(curToken.getType())){
                    check("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    check("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    check("R_BC");
                }
            }
        }
        if("LLremove".equals(curToken.getType())){
            check("LLremove");
            if("L_BC".equals(curToken.getType())){
                check("L_BC");
                if("VAR".equals(curToken.getType())){
                    check("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    check("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    check("R_BC");
                }
            }
        }
        if("LLget".equals(curToken.getType())){
            check("LLget");
            if("L_BC".equals(curToken.getType())){
                check("L_BC");
                if("VAR".equals(curToken.getType())){
                    check("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    check("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    check("R_BC");
                }
            }
        }
        if("LLsize".equals(curToken.getType())){
            check("LLsize");
            if("L_BC".equals(curToken.getType())){
                check("L_BC");
                if("VAR".equals(curToken.getType())){
                    check("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    check("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    check("R_BC");
                }
            }
        }
    }

    public void expr_assign() {
        assign();
        while ("DIV".equals(curToken.getType())) {
            check("DIV");
            assign();
        }
    }

    public void print() {
        check("PRINT");
        if ("L_BC".equals(curToken.getType())) {
            check("L_BC");
            if ("DIGIT".equals(curToken.getType())) {
                check("DIGIT");
            } else {
                check("VAR");
            }
            check("R_BC");
        }
    }

    public void linked_list() {
        check("LL");
        check("VAR");
    }

    public void linked_list_print(){
        check("PRNTLIST");
        if ("L_BC".equals(curToken.getType())) {
            check("L_BC");
            check("VAR");
            check("R_BC");
        }
    }
}