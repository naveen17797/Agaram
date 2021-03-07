package core;


import core.errors.TokenizerError;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Tokenizer extends Iterator<String> {




    public Tokenizer(String code) {
        super(Arrays.asList(code.split("")));
    }


    private int lineNumber = 1;

    private int position = 0;


    public boolean isTamilLetter(String c) {
        if (c.equals("'") || c.equals('"')) {
            return false;
        }
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c.charAt(0));
        return block.equals(Character.UnicodeBlock.TAMIL);
    }



    @Override
    public void advance() {
        super.advance();
        this.position += 1;
    }

    @Override
    boolean notAtEnd() {
        return current != (this.iterables.size());
    }

    public List<Token> getTokens() {
        List<Token> tokens = new Stack<Token>();
        while (notAtEnd()) {
            String currentChar = getCurrentItem();
            if ("'".equals(currentChar)) {
                advance();
                Token token = new Token();
                token.type = TokenType.STRING;
                // collect all tokens, until we find the next single quote.
                while (notAtEnd() && !getCurrentItem().equals("'")) {
                    token.value = token.value.concat(getCurrentItem());
                    advance();
                }
                // Remove the last ' char
                advance();
                tokens.add(token);
            } else if (isNumeric(currentChar)) {
                Token token = new Token(TokenType.NUMBER);
                while (notAtEnd() && ( isNumeric(getCurrentItem()) || getCurrentItem().equals("."))) {
                    token.value = token.value.concat(getCurrentItem());
                    advance();
                }
                tokens.add(token);
            } else if (currentChar.equals("+")) {
                tokens.add(new Token(TokenType.PLUS));
                advance();
            } else if (currentChar.equals("-")) {
                tokens.add(new Token(TokenType.MINUS));
                advance();
            } else if (currentChar.equals("*")) {
                tokens.add(new Token(TokenType.STAR));
                advance();
            } else if (currentChar.equals("/")) {

                // check if next character is slash too, then its a double line comment.
                if ( peekNext() != null && peekNext().equals("/")  ) {
                    advance();
                    advance();
                    // Loop through and collect all comment characters before new line.
                   while ( notAtEnd() && !getCurrentItem().equals("\n")) {
                       advance();
                   }
                }
                else {
                    tokens.add(new Token(TokenType.SLASH));
                    advance();
                }
            } else if (currentChar.equals("%")) {
                tokens.add(new Token(TokenType.MODULO));
                advance();
            } else if (currentChar.equals("\n")) {
                // increment the line number and reset the pos.
                this.lineNumber += 1;
                this.position = 0;
                advance();
            }
            else if  (currentChar.equals("(")) {
                tokens.add(new Token(TokenType.OPEN_BRACKET));
                advance();
            }
            else if  (currentChar.equals(")")) {
                tokens.add(new Token(TokenType.CLOSE_BRACKET));
                advance();
            }
            else if ( currentChar.equals("=")) {

                if ( peekNext()!= null && peekNext().equals("=")) {
                    advance();
                    advance();
                    tokens.add(new Token(TokenType.EQUAL_EQUAL));
                }
                else {
                    tokens.add(new Token(TokenType.EQUAL));
                    advance();
                }

            }
            else if  (currentChar.equals("<")) {
                if ( peekNext() != null && peekNext().equals("=")) {
                    advance();
                    advance();
                    tokens.add(new Token(TokenType.LESSER_OR_EQUAL_TO));
                }
                else {
                    tokens.add(new Token(TokenType.LESSER));
                    advance();
                }
            }
            else if  (currentChar.equals(">")) {

                if ( peekNext() != null && peekNext().equals("=")) {
                    advance();
                    advance();
                    tokens.add(new Token(TokenType.GREATER_OR_EQUAL_TO));
                }
                else {
                    tokens.add(new Token(TokenType.GREATER));
                    advance();
                }
            }
            else if  (currentChar.equals(">=")) {
                tokens.add(new Token(TokenType.GREATER_OR_EQUAL_TO));
                advance();
            }
            else if  (currentChar.equals(";")) {
                tokens.add(new Token(TokenType.SEMICOLON));
                advance();
            }
            else if  (currentChar.equals("{")) {
                tokens.add(new Token(TokenType.LEFT_BRACE));
                advance();
            }
            else if  (currentChar.equals("}")) {
                tokens.add(new Token(TokenType.RIGHT_BRACE));
                advance();
            }
            else if  (currentChar.equals(",")) {
                tokens.add(new Token(TokenType.COMMA));
                advance();
            }
            else if  (currentChar.equals("|")) {
                if ( peekNext() != null && peekNext().equals("|")) {
                    advance();
                    advance();
                    tokens.add(new Token(TokenType.LOGICAL_OR));
                }
                else {
                    advance();
                    throw new TokenizerError( lineNumber, position, "`||` எதிர்பார்க்கிறது ஆனால் கிடைத்தது `|`");
                }
            }
            else if  (currentChar.equals("&")) {
                if ( peekNext() != null && peekNext().equals("&")) {
                    advance();
                    advance();
                    tokens.add(new Token(TokenType.LOGICAL_AND));
                }
                else {
                    advance();
                    throw new TokenizerError( lineNumber, position, "`&&` எதிர்பார்க்கிறது ஆனால் கிடைத்தது `&`");
                }
            }


            else {
                if (currentChar.equals(" ")) {
                    advance();
                } else if (isTamilLetter(currentChar)) {
                    String keyword = "";
                    while (notAtEnd() && isTamilLetter(getCurrentItem())) {
                        keyword = keyword.concat(getCurrentItem());
                        advance();
                    }
                    Token keywordToken = new Token();
                    keywordToken.type = getTokenTypeFromKeyword(keyword);
                    keywordToken.value = keyword;
                    tokens.add(keywordToken);
                } else {
                    advance();
                    // Add tokenizer error.
                    throw new TokenizerError(lineNumber, position, "பிழையான எழுத்து `" + prev() + "`" );
                }
            }

        }

        tokens.add(new Token(TokenType.EOF));
        return tokens;

    }


    private boolean isAlphaNumeric(String string) {
        return string.matches("[A-Za-z0-9]+");
    }

    private boolean isNumeric(String currentChar) {
        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
        return numbers.contains(currentChar);
    }


    private TokenType getTokenTypeFromKeyword(String keyword) {
        if (keyword.equals("எழுது")) {
            return TokenType.PRINT;
        }
        else if ( keyword.equals("மாறி")) {
            return TokenType.VAR;
        }
        else if ( keyword.equals("ஒருவேளை")){
            return TokenType.IF;
        }
        else if ( keyword.equals("இல்லையென்றால்")){
            return TokenType.ELSE;
        }
        else if ( keyword.equals("அல்லது")){
            return TokenType.LOGICAL_OR;
        }
        else if ( keyword.equals("மற்றும்")){
            return TokenType.LOGICAL_AND;
        }
        else if ( keyword.equals("இருப்பின்")){
            return TokenType.WHILE;
        }
        else if (keyword.equals("உண்மை")) {
            return TokenType.TRUE;
        }
        else if (keyword.equals("பொய்")) {
            return TokenType.FALSE;
        }
        else if ( keyword.equals("ஆக") ) {
            return TokenType.FOR;
        }
        else if ( keyword.equals("செயல்பாடு") ) {
            return TokenType.FUNCTION;
        }
        else if ( keyword.equals("திருப்பு") ) {
            return TokenType.RETURN;
        }
        else {
            return TokenType.IDENTIFIER;
        }
    }



}
