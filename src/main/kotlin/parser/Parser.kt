package parser

import nodes.*
import nodes.expressions.BooleanRF
import nodes.expressions.Identifier
import nodes.expressions.IntegerRF
import nodes.expressions.StringRF
import nodes.expressions.expression.Comparison
import nodes.expressions.expression.Equality
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.statements.Repeat
import nodes.statements.VarAssign
import nodes.statements.VarDec
import tokens.Token
import tokens.TokenType
import kotlin.system.exitProcess

class Parser(tokenStream: List<Token>) {

    private var currentIndex = 0
    private var currentToken: Token
    private val tokenStream: List<Token>

    init {
        this.tokenStream = tokenStream
        this.currentToken = tokenStream[currentIndex]
    }

    fun parse(): Program {
        return Program(parseStmtList())
    }

    private fun parseStmtList(): List<Statement> {
        var stmtList = emptyList<Statement>()
        while (currentToken.getType() != TokenType.EOF) {
            stmtList = stmtList.plus(parseStmt())
        }
        return stmtList
    }

    private fun parseBodyStmtList(): List<Statement> {
        var stmtList = emptyList<Statement>()
        while (currentToken.getType() != TokenType.RIGHT_CURLY_BRACE && currentToken.getType() != TokenType.EOF) {
            stmtList = stmtList.plus(parseStmt())
        }
        return stmtList
    }

    private fun parseStmt(): Statement {
        when (currentToken.getType()) {
            TokenType.KEYWORD_LET -> return parseVarDeclarationStmt()
            TokenType.KEYWORD_REPEAT -> return parseRepeatStmt()
            TokenType.KEYWORD_WHILE -> return parseWhileStmt()
            TokenType.KEYWORD_FUN -> return parseFunctionDeclarationStmt()
            TokenType.KEYWORD_RETURN -> return parseReturnStmt()
            TokenType.IDENTIFIER -> return parseVarAssignmentStmt()
            else -> {
                println("Invalid statement")
                exitProcess(0)
            }
        }
    }

    private fun parseVarDeclarationStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_LET)
        val token = matchAndConsume(TokenType.IDENTIFIER)
        matchAndConsume(TokenType.EQ)

        val value: Expression = when (currentToken.getType()) {
            TokenType.INTEGER_LITERAL -> {
                val int = currentToken.getLiteral().toInt()
                IntegerRF(int).also {
                    consume()
                }
            }
            TokenType.STRING_LITERAL -> {
                StringRF(currentToken.getLiteral()).also {
                    consume()
                }
            }
            TokenType.KEYWORD_TRUE -> {
                BooleanRF(true).also {
                    consume()
                }
            }
            TokenType.KEYWORD_FALSE -> {
                BooleanRF(false).also {
                    consume()
                }
            }
            TokenType.IDENTIFIER -> {
                Identifier(currentToken.getLiteral()).also {
                    consume()
                }
            }
            else -> parseExpr()
        }
        return VarDec(token.getLiteral(), value)
    }

    private fun parseRepeatStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_REPEAT)
        val value: Expression = when (currentToken.getType()) {
            TokenType.INTEGER_LITERAL -> {
                val int = currentToken.getLiteral().toInt()
                IntegerRF(int).also {
                    consume()
                }
            }
            TokenType.IDENTIFIER -> {
                Identifier(currentToken.getLiteral()).also {
                    consume()
                }
            }
            else -> parseExpr()
        }
        matchAndConsume(TokenType.LEFT_CURLY_BRACE)
        val stmts = parseBodyStmtList()
        matchAndConsume(TokenType.RIGHT_CURLY_BRACE)

        return Repeat(value, stmts)
    }

    private fun parseWhileStmt(): Statement {
        return VarDec("stub", BooleanRF(true))
    }

    private fun parseFunctionDeclarationStmt(): Statement {
        return VarDec("stub", BooleanRF(true))
    }

    private fun parseReturnStmt(): Statement {
        return VarDec("stub", BooleanRF(true))
    }

    private fun parseVarAssignmentStmt(): Statement {
        val token = matchAndConsume(TokenType.IDENTIFIER)
        matchAndConsume(TokenType.EQ)

        val value: Expression = when (currentToken.getType()) {
            TokenType.INTEGER_LITERAL -> {
                val int = currentToken.getLiteral().toInt()
                IntegerRF(int).also {
                    consume()
                }
            }
            TokenType.STRING_LITERAL -> {
                StringRF(currentToken.getLiteral()).also {
                    consume()
                }
            }
            TokenType.KEYWORD_TRUE -> {
                BooleanRF(true).also {
                    consume()
                }
            }
            TokenType.KEYWORD_FALSE -> {
                BooleanRF(false).also {
                    consume()
                }
            }
            TokenType.IDENTIFIER -> {
                Identifier(currentToken.getLiteral()).also {
                    consume()
                }
            }
            else -> parseExpr()
        }

        return VarAssign(token.getLiteral(), value)
    }

    // Helpers
    private fun consume() {
        currentIndex += 1
        currentToken = tokenStream[currentIndex]
    }

    private fun peek(): Token? {
        return tokenStream.getOrNull(currentIndex + 1)
    }

    private fun match(tokenType: TokenType): Boolean {
        if (currentToken.getType() == tokenType) {
            return true
        }
        return false
    }

    private fun matchAndConsume(tokenType: TokenType): Token {
        val currentTokenTemp = currentToken
        if (currentToken.getType() == tokenType) {
            consume()
            return currentTokenTemp
        } else {
            println("Expected to match token '$tokenType' but got '${currentToken.getType()}'")
            exitProcess(0)
        }
    }

    // Expression Parsings
    private fun parseExpr(): Expression {
        return parseEquality()
    }

    private fun parseEquality(): Expression {
        var expr = parseComparison()
        return Comparison()
    }

    private fun parseComparison(): Expression {
        return Comparison()
    }

}