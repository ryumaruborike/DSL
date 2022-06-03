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

    public void terminalCheck(String tokenType) {
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
        terminalCheck("ENDLINE");
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
            default -> terminalCheck("VAR");
        }
    }

    public void expr_value() {
        switch (curToken.getType()) {
            case "VAR", "DIGIT" -> value();
            case "L_BC" -> infinity();
            default -> terminalCheck("VAR");
        }
        while ("OPERATOR".equals(curToken.getType())) {
            terminalCheck("OPERATOR");
            value();
        }
    }

    public void value() {
        switch (curToken.getType()) {
            case "DIGIT" -> terminalCheck("DIGIT");
            case "L_BC" -> infinity();
            default -> terminalCheck("VAR");
        }
    }

    public void infinity() {
        terminalCheck("L_BC");
        expr_value();
        terminalCheck("R_BC");
    }

    public void condition() {
        terminalCheck("VAR");
        terminalCheck("COMPARISON_OP");
        expr_value();
    }

    public void condition_in_br() {
        terminalCheck("L_BC");
        condition();
        terminalCheck("R_BC");
    }

    public void if_op() {
        terminalCheck("IF_OPERATION");
        condition_in_br();
        do {
            body();
        } while (body_condition());
        if ("ELSE".equals(curToken.getType())) {
            else_op();
        }
    }

    public void else_op() {
        terminalCheck("ELSE");
        do {
            expr();
        } while (body_condition());
    }

    public void while_op() {
        terminalCheck("WHILE");
        condition_in_br();
        do {
            body();
        } while (body_condition());
    }

    public void do_while_op() {
        terminalCheck("DO_WHILE");
        do {
            body();
        } while (body_condition_do_while());
        terminalCheck("WHILE");
        condition_in_br();
    }

    public void for_op() {
        terminalCheck("FOR");
        terminalCheck("L_BC");
        terminalCheck("VAR");
        assign();
        terminalCheck("DIV");
        condition();
        terminalCheck("DIV");
        terminalCheck("VAR");
        assign();
        terminalCheck("R_BC");
        do {
            body();
        } while (body_condition());
    }

    public void assign() {
        //terminalCheck("VAR");
        terminalCheck("ASSIGNMENT_OPERATOR");
        expr_value();
    }

    public void var() {
        terminalCheck("VAR");
        if("POINT".equals(curToken.getType())){
            ll_operate();
        }
        else if("ASSIGNMENT_OPERATOR".equals(curToken.getType())){
            expr_assign();
        }
    }

    public void ll_operate(){
        //terminalCheck("VAR");
        terminalCheck("POINT");
        if("LLadd".equals(curToken.getType())){
            terminalCheck("LLadd");
            if("L_BC".equals(curToken.getType())){
                terminalCheck("L_BC");
                if("VAR".equals(curToken.getType())){
                    terminalCheck("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    terminalCheck("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    terminalCheck("R_BC");
                }
            }
        }
        if("LLremove".equals(curToken.getType())){
            terminalCheck("LLremove");
            if("L_BC".equals(curToken.getType())){
                terminalCheck("L_BC");
                if("VAR".equals(curToken.getType())){
                    terminalCheck("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    terminalCheck("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    terminalCheck("R_BC");
                }
            }
        }
        if("LLget".equals(curToken.getType())){
            terminalCheck("LLget");
            if("L_BC".equals(curToken.getType())){
                terminalCheck("L_BC");
                if("VAR".equals(curToken.getType())){
                    terminalCheck("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    terminalCheck("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    terminalCheck("R_BC");
                }
            }
        }
        if("LLsize".equals(curToken.getType())){
            terminalCheck("LLsize");
            if("L_BC".equals(curToken.getType())){
                terminalCheck("L_BC");
                if("VAR".equals(curToken.getType())){
                    terminalCheck("VAR");
                }
                else if("DIGIT".equals(curToken.getType())) {
                    terminalCheck("DIGIT");
                }
                if("R_BC".equals(curToken.getType())){
                    terminalCheck("R_BC");
                }
            }
        }
    }

    public void expr_assign() {
        assign();
        while ("DIV".equals(curToken.getType())) {
            terminalCheck("DIV");
            assign();
        }
    }

    public void print() {
        terminalCheck("PRINT");
        if ("L_BC".equals(curToken.getType())) {
            terminalCheck("L_BC");
            if ("DIGIT".equals(curToken.getType())) {
                terminalCheck("DIGIT");
            } else {
                terminalCheck("VAR");
            }
            terminalCheck("R_BC");
        }
    }

    public void linked_list() {
        terminalCheck("LL");
        terminalCheck("VAR");
    }

    public void linked_list_print(){
        terminalCheck("PRNTLIST");
        if ("L_BC".equals(curToken.getType())) {
            terminalCheck("L_BC");
            terminalCheck("VAR");
            terminalCheck("R_BC");
        }
    }
}