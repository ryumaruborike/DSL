public class ParseExc extends Exception{
    String expected;
    Token token;
    public void getMsg(Token currentToken, String expected){
        System.out.println("got: " + currentToken.type + " but expected: " + expected);
    }
    public ParseExc(Token currentToken, String expected){
        this.expected = expected;
        this.token = currentToken;
        System.out.println();
    }
}
