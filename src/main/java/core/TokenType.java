package core;

public enum TokenType {
    STRING,
    KEYWORD,
    PRINT,
    NUMBER,


    /**
     * Character from ancient wisdom
     */
    SEMICOLON,
    COMMA,

    /**
     * Group operator `(` `)`
     */
    OPEN_BRACKET,
    CLOSE_BRACKET,

    /**
     * Comparsion operators
     */
    GREATER,
    LESSER,
    GREATER_OR_EQUAL_TO,
    LESSER_OR_EQUAL_TO,
    EQUAL_EQUAL,



    /**
     * Numerical Operators.
     */
    PLUS,
    MINUS,
    STAR,
    SLASH,
    MODULO,
    // End of file.
    EOF, VAR, IDENTIFIER, EQUAL,


    /**
     * Braces
     */
    LEFT_BRACE,
    RIGHT_BRACE,

    /**
     * Logical operations
     */
    LOGICAL_OR,
    LOGICAL_AND,

    /**
     * Conditional tokens
     */
    IF,
    ELSE,
    WHILE,


    /**
     * Primitive types.
     */
    TRUE,
    FALSE,


    FOR, UNEXPECTED_IDENTIFIER,



    /**
     * Function declaration
     */
    RETURN,
    FUNCTION
}
