package scanner

import tokens.Token
import tokens.TokenType
import java.io.InputStreamReader
import kotlin.system.exitProcess

private const val EOF = -1

class Scanner constructor(streamReader: InputStreamReader) {

    private var currentChar: Char
    private var currentCharValue: Int
    private val streamReader: InputStreamReader
    private var tokenStream: List<Token>

    init {
        this.streamReader = streamReader
        tokenStream = emptyList()
        currentCharValue = streamReader.read()
        currentChar = currentCharValue.toChar()
    }

    fun scan(): List<Token> {
        while (currentCharValue != EOF) {
            if (currentChar.isLetter()) {
                parseCharStream()
            } else if (currentChar.isDigit()) {
                parseDigitStream()
            } else if (currentChar.isWhitespace()) {
                skipWhitespace()
            } else if (currentChar == '"') {
                parseStringLiteral()
            } else if (currentChar == '=') {
                parseEqOrAssignment()
            } else if (currentChar == '!') {
                parseBangOrBangEq()
            } else if (currentChar == '<') {
                parseLTOrLTEq()
            } else if (currentChar == '>') {
                parseGTOrGTEq()
            } else {
                when (currentChar) {
                    '(' -> tokenStream = tokenStream.plus(Token(TokenType.LEFT_PAREN, currentChar.toString()))
                    ')' -> tokenStream = tokenStream.plus(Token(TokenType.RIGHT_PAREN, currentChar.toString()))
                    '{' -> tokenStream = tokenStream.plus(Token(TokenType.LEFT_CURLY_BRACE, currentChar.toString()))
                    '}' -> tokenStream = tokenStream.plus(Token(TokenType.RIGHT_CURLY_BRACE, currentChar.toString()))
                    ',' -> tokenStream = tokenStream.plus(Token(TokenType.COMMA, currentChar.toString()))
                    '+' -> tokenStream = tokenStream.plus(Token(TokenType.PLUS, currentChar.toString()))
                    '-' -> tokenStream = tokenStream.plus(Token(TokenType.MINUS, currentChar.toString()))
                    '*' -> tokenStream = tokenStream.plus(Token(TokenType.MULTIPLY, currentChar.toString()))
                    '/' -> tokenStream = tokenStream.plus(Token(TokenType.DIVIDE, currentChar.toString()))
                    else -> {
                        //TODO Support better error handling
                        println("illegal character: '$currentChar'")
                        exitProcess(0)
                    }
                }
                nextChar()
            }
        }

        // Add EOF token
        tokenStream = tokenStream.plus(Token(TokenType.EOF, String()))
        return tokenStream
    }

    private fun nextChar() {
        currentCharValue = streamReader.read()
        currentChar = currentCharValue.toChar()
    }

    private fun parseCharStream() {
        var result = ""
        while (currentCharValue != EOF && (currentChar.isLetter() || currentChar.isDigit())) {
            result += currentChar
            nextChar()
        }

        constants.KEYWORDS_MAP[result]?.let {
            tokenStream = tokenStream.plus(Token(it, result))
            return
        }

        tokenStream = tokenStream.plus(Token(TokenType.IDENTIFIER, result))
    }

    private fun parseDigitStream() {
        var result = ""
        while (currentCharValue != EOF && currentChar.isDigit()) {
            result += currentChar
            nextChar()
        }

        tokenStream = tokenStream.plus(Token(TokenType.INTEGER_LITERAL, result))
    }

    private fun skipWhitespace() {
        while (currentCharValue != EOF && currentChar.isWhitespace()) {
            nextChar()
        }
    }

    private fun parseStringLiteral() {
        nextChar()
        var result = ""
        while (currentCharValue != EOF && currentChar != '"') {
            result += currentChar
            nextChar()
        }

        //non-terminated string
        if (currentCharValue == EOF) {
            //TODO handle errors better
            println("non-terminated string caused fatal error")
            exitProcess(0)
        }

        nextChar()
        tokenStream = tokenStream.plus(Token(TokenType.STRING_LITERAL, result))
    }

    private fun parseEqOrAssignment() {
        val prevChar = currentChar
        nextChar()
        if (currentChar == '=') {
            tokenStream = tokenStream.plus(Token(TokenType.EQ_EQ, "${prevChar}${currentChar}"))
            nextChar()
        } else {
            tokenStream = tokenStream.plus(Token(TokenType.EQ, "$prevChar"))
        }
    }

    private fun parseBangOrBangEq() {
        val prevChar = currentChar
        nextChar()
        if (currentChar == '=') {
            tokenStream = tokenStream.plus(Token(TokenType.BANG_EQ, "${prevChar}${currentChar}"))
            nextChar()
        } else {
            tokenStream = tokenStream.plus(Token(TokenType.BANG, "$prevChar"))
        }
    }

    private fun parseLTOrLTEq() {
        val prevChar = currentChar
        nextChar()
        if (currentChar == '=') {
            tokenStream = tokenStream.plus(Token(TokenType.LT_EQ, "${prevChar}${currentChar}"))
            nextChar()
        } else {
            tokenStream = tokenStream.plus(Token(TokenType.LT, "$prevChar"))
        }
    }

    private fun parseGTOrGTEq() {
        val prevChar = currentChar
        nextChar()
        if (currentChar == '=') {
            tokenStream = tokenStream.plus(Token(TokenType.GT_EQ, "${prevChar}${currentChar}"))
            nextChar()
        } else {
            tokenStream = tokenStream.plus(Token(TokenType.GT, "$prevChar"))
        }
    }
}