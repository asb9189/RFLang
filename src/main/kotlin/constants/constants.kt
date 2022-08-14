package constants

import tokens.TokenType

const val KEYWORD_LET = "let"
const val KEYWORD_AND = "and"
const val KEYWORD_OR = "or"
const val KEYWORD_REPEAT = "repeat"
const val KEYWORD_WHILE = "while"
const val KEYWORD_FUN = "fun"
const val KEYWORD_RETURN = "return"
const val KEYWORD_TRUE = "true"
const val KEYWORD_FALSE = "false"
const val KEYWORD_IF = "if"
const val KEYWORD_ELIF = "elif"
const val KEYWORD_ELSE = "else"
const val KEYWORD_BREAK = "break"

val KEYWORDS_MAP = hashMapOf(
    KEYWORD_LET to TokenType.KEYWORD_LET,
    KEYWORD_AND to TokenType.KEYWORD_AND,
    KEYWORD_OR to TokenType.KEYWORD_OR,
    KEYWORD_REPEAT to TokenType.KEYWORD_REPEAT,
    KEYWORD_WHILE to TokenType.KEYWORD_WHILE,
    KEYWORD_FUN to TokenType.KEYWORD_FUN,
    KEYWORD_RETURN to TokenType.KEYWORD_RETURN,
    KEYWORD_TRUE to TokenType.KEYWORD_TRUE,
    KEYWORD_FALSE to TokenType.KEYWORD_FALSE,
    KEYWORD_IF to TokenType.KEYWORD_IF,
    KEYWORD_ELIF to TokenType.KEYWORD_ELIF,
    KEYWORD_ELSE to TokenType.KEYWORD_ELSE,
    KEYWORD_BREAK to TokenType.KEYWORD_BREAK
)