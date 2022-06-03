import java.util.ArrayList;

public class Parser {
    int iterator = 0;
    public ArrayList<Token> tokens;
    public int len;
    Parser(ArrayList<Token> tokens, int len) {
        this.tokens = tokens;
        this.len = len;
    }

    public void lang() throws ParseExc {
        for (int i = 0;i < len; i++ ){
            expr_();
        }
    }
    public void expr_() throws ParseExc {
        Token currentToken = tokens.get(iterator);
        if (currentToken.type.equals("WHILE")){
            while_do(currentToken);
            currentToken = tokens.get(iterator);
        }
        if (currentToken.type.equals("DO_WHILE")){
            do_while(currentToken);
            currentToken = tokens.get(iterator);
        }
        if (currentToken.type.equals("IF_OPERATION")){
            try {
                IF(currentToken);
            }
            catch (ParseExc ex){
                ex.getMsg(ex.token, ex.expected);
            }
            iterator++;
            currentToken = tokens.get(iterator);
            try{
                LB(currentToken);
            }
            catch (ParseExc ex){
                ex.getMsg(ex.token, ex.expected);
            }
            iterator++;
            currentToken = tokens.get(iterator);
            condition(currentToken);
            currentToken = tokens.get(iterator);
            try{
                RB(currentToken);
            }
            catch (ParseExc ex){
                ex.getMsg(ex.token, ex.expected);
            }
            iterator++;
            currentToken = tokens.get(iterator);
        }
        try {
            var__(currentToken);
        }
        catch (ParseExc ex){
            ex.getMsg(currentToken, "VAR");
        }
        iterator++;
        currentToken = tokens.get(iterator);
        try{
            assign_op(currentToken);
        }
        catch (ParseExc ex){
            ex.getMsg(ex.token, ex.expected);
        }
        iterator++;
        currentToken = tokens.get(iterator);
        while ((!currentToken.type.equals("ENDLINE")) & (!currentToken.type.equals("R_BC")) & (!currentToken.type.equals("L_BC")) & (!currentToken.type.equals("WHILE"))){
            expr_val(currentToken);
            iterator++;
            currentToken = tokens.get(iterator);
        }
        if (currentToken.type.equals("WHILE")){
            try {
                WHILE(currentToken);
            }
            catch(ParseExc ex){
                ex.getMsg(ex.token, ex.expected);
            }
            iterator++;
            currentToken = tokens.get(iterator);
            if (currentToken.type.equals("L_BC")){
                try{
                    LB(currentToken);
                }
                catch (ParseExc ex){
                    ex.getMsg(ex.token, ex.expected);
                }
                iterator++;
                currentToken = tokens.get(iterator);
                condition(currentToken);
                currentToken = tokens.get(iterator);
                try{
                    RB(currentToken);
                }
                catch (ParseExc ex){
                    ex.getMsg(ex.token, ex.expected);
                }
                iterator++;
            }
        }
        try{
            currentToken = tokens.get(iterator);
            ENDLINE(currentToken);
        }
        catch (ParseExc ex){
            ex.getMsg(ex.token, ex.expected);
        }
        iterator++;
    }

    public void IF(Token currentToken) throws ParseExc {
        if (!currentToken.type.equals("IF_OPERATION"))
            throw new ParseExc(currentToken, "IF_OPERATION");
    }

    public void var__(Token currentToken) throws ParseExc {
            if (!currentToken.type.equals("VAR"))
                throw new ParseExc(currentToken, "VAR");
    }
    public void assign_op(Token currentToken) throws ParseExc {
             if (!currentToken.type.equals("ASSIGNMENT_OPERATOR"))
             {
                throw new ParseExc(currentToken, "ASSIGNMENT_OPERATOR");
             }
    }
    public void expr_val(Token currentToken) throws ParseExc {
        if ((currentToken.type.equals("VAR")) | (currentToken.type.equals("DIGIT")))
            value(currentToken);
        else
            try {
                OP_VALUE(currentToken);
            }
            catch(ParseExc ex){
                ex.getMsg(ex.token, ex.expected);
            }

    }
    public void value(Token currentToken) throws ParseExc {
        if (currentToken.type.equals("VAR"))
            var__(currentToken);
        else
            try{
                digit__(currentToken);
            }
            catch (ParseExc ex){
                ex.getMsg(ex.token, ex.expected);
            }
    }
    public void digit__(Token currentToken) throws ParseExc{
        if (!currentToken.type.equals("DIGIT"))
            throw new ParseExc(currentToken, "DIGIT");
    }
    public void OP_VALUE(Token currentToken) throws ParseExc{
        if (currentToken.type.equals("OPERATOR"))
            throw new ParseExc(currentToken, "OPERATOR");
    }
    public void while_do(Token currentToken) throws ParseExc {
        WHILE(currentToken);
        iterator++;
        currentToken = tokens.get(iterator);
        try{
            LB(currentToken);
        }
        catch (ParseExc ex){
            ex.getMsg(ex.token, ex.expected);
        }
        iterator++;
        currentToken = tokens.get(iterator);
        condition(currentToken);
        currentToken = tokens.get(iterator);
        try {
            RB(currentToken);
        }
        catch (ParseExc ex){
            ex.getMsg(ex.token, ex.expected);
        }
        iterator++;
    }
    public void LB(Token currentToken) throws ParseExc {
        if (!currentToken.type.equals("L_BC"))
            throw new ParseExc(currentToken, "L_BC");
    }
    public void RB(Token currentToken) throws ParseExc{
        if (!currentToken.type.equals("R_BC"));
            throw new ParseExc(currentToken, "R_BC");
    }
    public void condition(Token currentToken) throws ParseExc {
        try {
            var__(currentToken);
        }
        catch (ParseExc ex){
            ex.getMsg(ex.token, ex.expected);
        }
        iterator++;
        currentToken = tokens.get(iterator);
        try {
            COMPARISON_OP(currentToken);
        }
        catch (ParseExc ex){
            ex.getMsg(ex.token, ex.expected);
        }
        iterator++;
        currentToken = tokens.get(iterator);
        expr_val(currentToken);
        iterator++;
    }
    public void COMPARISON_OP (Token currentToken) throws ParseExc{
        if (!currentToken.type.equals("COMPARISON_OP"))
            throw new ParseExc(currentToken, "COMPARISON_OP");
    }
    public void WHILE(Token currentToken) throws ParseExc{
        if (!currentToken.type.equals("WHILE"))
            throw new ParseExc(currentToken, "WHILE");
    }
    public void ENDLINE(Token currentToken) throws ParseExc{
        if (!currentToken.type.equals("ENDLINE"))
            throw new ParseExc(currentToken, "ENDLINE");
    }
    public void do_while(Token currentToken) throws ParseExc{
        DO(currentToken);
        iterator++;
    }
    public void DO(Token currentToken) throws ParseExc{
        if (!currentToken.type.equals("DO_WHILE"))
            throw new ParseExc(currentToken, "DO");
    }
}
//lang->expr+
//expr->(if|while_do|do_while) (WHIlE LB condition RB)? ASSIGN_OP (expr_val)+ ENDLINE
//if->IF LB condition RB
//while_do-> WHILE LB condition RB
//do_while->DO
//condition-> VAR COMPARISON_OP (expr_val)+
//expr_val->value | OP_VALUE
//value-> VAR | DIGIT
