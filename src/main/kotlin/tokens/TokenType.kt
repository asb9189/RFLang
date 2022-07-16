package tokens

enum class TokenType {
    KEYWORD_LET,
    KEYWORD_AND, // BinOp.kt
    KEYWORD_OR, // BinOp.kt
    KEYWORD_FUN,
    KEYWORD_REPEAT,
    KEYWORD_WHILE,
    KEYWORD_RETURN,
    KEYWORD_TRUE,
    KEYWORD_FALSE,
    IDENTIFIER,
    INTEGER_LITERAL,
    STRING_LITERAL,
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_CURLY_BRACE,
    RIGHT_CURLY_BRACE,
    COMMA,
    PLUS, // BinOp.kt
    MINUS, // BinOp.kt
    MULTIPLY, // BinOp.kt
    DIVIDE, // BinOp.kt
    ASSIGN,
    EQ, // BinOp.kt
    EOF
}