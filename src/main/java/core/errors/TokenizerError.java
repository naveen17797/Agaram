package core.errors;


public class TokenizerError extends RuntimeException{
    private final int lineNumber;
    private final int position;
    private final String message;


    public TokenizerError(int lineNumber, int position, String message) {
        super(null, null, false, false);
        this.lineNumber = lineNumber;
        this.position = position;
        this.message = message;

    }

    @Override
    public String getMessage() {
        return "டோக்கனைசர் பிழை : வரி எண்: " + lineNumber + " எழுத்து எண்: " + position + " => " + message;
    }
}