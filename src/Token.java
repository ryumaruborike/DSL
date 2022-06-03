public class Token {
    String type;
    String token;
    Token(String Type, String Token){
        type = Type;
        token = Token;
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}