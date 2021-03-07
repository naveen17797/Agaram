package core;

public class Token {

    public Token( TokenType type ) {
        this.type = type;
    }

    public Token() {

    }


    public TokenType type;

    public String value = "";


    public int lineNumber = -1;

    public int position = -1;

}
