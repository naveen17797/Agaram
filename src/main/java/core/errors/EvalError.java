package core.errors;

import core.Token;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvalError  extends RuntimeException{
    private final String message;
    private final Token token;

    @Override
    public String getMessage() {
        return "வரி எண்: " + token.lineNumber + " எழுத்து எண்: " + token.position + " => " + message;
    }
}
