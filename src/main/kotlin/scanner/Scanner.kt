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
            } else if (currentChar == '"') {
                parseStringLiteral()
            } else {}
        }
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

        tokenStream.plus(Token(TokenType.INTEGER_LITERAL, result))
    }

    private fun parseStringLiteral() {
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
        tokenStream.plus(Token(TokenType.STRING_LITERAL, result))
    }
}