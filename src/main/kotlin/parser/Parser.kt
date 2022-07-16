package parser

import nodes.*
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.statements.VarDec
import tokens.Token
import tokens.TokenType
import javax.swing.plaf.nimbus.State
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
        return VarDec()
    }

    private fun parseRepeatStmt(): Statement {
        return VarDec()
    }

    private fun parseWhileStmt(): Statement {
        return VarDec()
    }

    private fun parseFunctionDeclarationStmt(): Statement {
        return VarDec()
    }

    private fun parseReturnStmt(): Statement {
        return VarDec()
    }

    private fun parseVarAssignmentStmt(): Statement {
        return VarDec()
    }

    // Helpers
    private fun peek(): Token? {
        return tokenStream.getOrNull(currentIndex + 1)
    }
}