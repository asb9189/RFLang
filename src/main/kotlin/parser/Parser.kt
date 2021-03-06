package parser

import nodes.*
import nodes.expressions.*
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.statements.*
import tokens.Token
import tokens.TokenType
import kotlin.system.exitProcess

class Parser(tokenStream: List<Token>) {

    enum class FUNCTION {
        EXPRESSION,
        STATEMENT
    }

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
            TokenType.KEYWORD_IF -> return parseIfStmt()
            TokenType.IDENTIFIER -> {
                if (peek()?.getType() == TokenType.LEFT_PAREN) {
                    return parseFunctionCall(FUNCTION.STATEMENT) as FuncCallStmt
                }
                return parseVarAssignmentStmt()
            }
            else -> {
                println("Invalid statement in parse stmt")
                exitProcess(0)
            }
        }
    }

    private fun parseVarDeclarationStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_LET)
        val token = matchAndConsume(TokenType.IDENTIFIER)
        matchAndConsume(TokenType.EQ)

        val value = parseExpression()
        return VarDec(token.getLiteral(), value)
    }

    private fun parseRepeatStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_REPEAT)
        val value = parseExpression()

        matchAndConsume(TokenType.LEFT_CURLY_BRACE)
        val stmts = parseBodyStmtList()
        matchAndConsume(TokenType.RIGHT_CURLY_BRACE)

        return Repeat(value, stmts)
    }

    private fun parseWhileStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_WHILE)
        val value = parseExpression()

        matchAndConsume(TokenType.LEFT_CURLY_BRACE)
        val stmts = parseBodyStmtList()
        matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
        return While(value, stmts)
    }

    private fun parseConstructorCall(): Expression {
        val constructorName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
        matchAndConsume(TokenType.LEFT_BRACKET)

        if (currentToken.getType() == TokenType.RIGHT_BRACKET) {
            matchAndConsume(TokenType.RIGHT_BRACKET)
            return ConstructorCallExpr(constructorName, emptyList())
        }

        val arguments = parseArguments()
        return ConstructorCallExpr(constructorName, arguments)
    }

    private fun parseFunctionCall(func: FUNCTION): Any {
        val functionName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
        matchAndConsume(TokenType.LEFT_PAREN)

        if (currentToken.getType() == TokenType.RIGHT_PAREN) {
            matchAndConsume(TokenType.RIGHT_PAREN)
            return when (func) {
                FUNCTION.STATEMENT -> FuncCallStmt(functionName, emptyList())
                FUNCTION.EXPRESSION -> FuncCallExpr(functionName, emptyList())
            }
        }

        val arguments = parseArguments()

        return when (func) {
            FUNCTION.STATEMENT -> FuncCallStmt(functionName, arguments)
            FUNCTION.EXPRESSION -> FuncCallExpr(functionName, arguments)
        }
    }

    private fun parseArguments(): List<Expression> {
        var args = emptyList<Expression>()
        args = args.plus(parseExpression())

        while (currentToken.getType() == TokenType.COMMA) {
            matchAndConsume(TokenType.COMMA)
            args = args.plus(parseExpression())
        }
        matchAndConsume(TokenType.RIGHT_PAREN)
        return args
    }

    private fun parseFunctionDeclarationStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_FUN).getLiteral()
        val functionName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
        matchAndConsume(TokenType.LEFT_PAREN)

        if (currentToken.getType() == TokenType.RIGHT_PAREN) {
            matchAndConsume(TokenType.RIGHT_PAREN)
            matchAndConsume(TokenType.LEFT_CURLY_BRACE)

            val stmts = parseBodyStmtList()
            matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
            return FuncDef(functionName, emptyList(), stmts)
        }

        val params = parseParams()
        matchAndConsume(TokenType.RIGHT_PAREN)
        matchAndConsume(TokenType.LEFT_CURLY_BRACE)
        val stmts = parseBodyStmtList()
        matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
        return FuncDef(functionName, params, stmts)
    }

    private fun parseParams(): List<String> {
        var params = emptyList<String>()
        params = params.plus(matchAndConsume(TokenType.IDENTIFIER).getLiteral())

        while (currentToken.getType() == TokenType.COMMA) {
            matchAndConsume(TokenType.COMMA)
            params = params.plus(matchAndConsume(TokenType.IDENTIFIER).getLiteral())
        }
        return params
    }

    private fun parseReturnStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_RETURN)
        return Return(parseExpression())
    }

    private fun parseVarAssignmentStmt(): Statement {
        val token = matchAndConsume(TokenType.IDENTIFIER)
        matchAndConsume(TokenType.EQ)

        val value = parseExpression()

        return VarAssign(token.getLiteral(), value)
    }

    private fun parseIfStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_IF)
        val ifCondition = parseExpression()
        matchAndConsume(TokenType.LEFT_CURLY_BRACE)
        val ifStmts = parseBodyStmtList()
        matchAndConsume(TokenType.RIGHT_CURLY_BRACE)

        return when (currentToken.getType()) {
            TokenType.KEYWORD_ELIF -> {
                val elseIfList = parseElseIfStmts()
                return if (currentToken.getType() == TokenType.KEYWORD_ELSE) {
                    matchAndConsume(TokenType.KEYWORD_ELSE)
                    matchAndConsume(TokenType.LEFT_CURLY_BRACE)
                    val elseStmts = parseBodyStmtList()
                    matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
                    If(ifCondition, ifStmts, elseIfList, elseStmts)
                } else {
                    If(ifCondition, ifStmts, elseIfList, emptyList())
                }
            }
            TokenType.KEYWORD_ELSE -> {
                matchAndConsume(TokenType.KEYWORD_ELSE)
                matchAndConsume(TokenType.LEFT_CURLY_BRACE)
                val elseStmts = parseBodyStmtList()
                matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
                If(ifCondition, ifStmts, emptyList(), elseStmts)
            }
            else -> {
                If(ifCondition, ifStmts, emptyList(), emptyList())
            }
        }
    }

    private fun parseElseIfStmts(): List<ElseIf> {
        var list = emptyList<ElseIf>()
        while (currentToken.getType() == TokenType.KEYWORD_ELIF) {
            consume()
            val condition = parseExpression()
            matchAndConsume(TokenType.LEFT_CURLY_BRACE)
            val stmts = parseBodyStmtList()
            matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
            list = list.plus(ElseIf(condition, stmts))
        }
        return list
    }

    // Helpers
    private fun consume() {
        currentIndex += 1
        currentToken = tokenStream[currentIndex]
    }

    private fun peek(): Token? {
        return tokenStream.getOrNull(currentIndex + 1)
    }

    private fun previous(): Token {
        return tokenStream[currentIndex - 1]
    }

    private fun match(tokenType: TokenType): Boolean {
        if (currentToken.getType() == tokenType) {
            consume()
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
    private fun parseExpression(): Expression {
        return parseEquality()
    }

    private fun parseEquality(): Expression {
        var expr = parseComparison()

        while (match(TokenType.BANG_EQ) || match(TokenType.EQ_EQ)) {
            val operator = previous().getType()
            val right = parseComparison()
            expr = BinOp(expr, operator, right)
        }
        return expr
    }

    private fun parseComparison(): Expression {
        var expr = parseTerm()

        while (match(TokenType.GT) || match(TokenType.GT_EQ) || match(TokenType.LT) || match(TokenType.LT_EQ) ||
            match(TokenType.KEYWORD_AND) || match(TokenType.KEYWORD_OR)) {
            val operator = previous().getType()
            val right = parseTerm()
            expr = BinOp(expr, operator, right)
        }
        return expr
    }

    private fun parseTerm(): Expression {
        var expr = parseFactor()

        while (match(TokenType.MINUS) || match(TokenType.PLUS)) {
            val operator = previous().getType()
            val right = parseFactor()
            expr = BinOp(expr, operator, right)
        }
        return expr
    }

    private fun parseFactor(): Expression {
        var expr = parseUnary()

        while (match(TokenType.DIVIDE) || match(TokenType.MULTIPLY)) {
            val operator = previous().getType()
            val right = parseUnary()
            expr = BinOp(expr, operator, right)
        }
        return expr
    }

    private fun parseUnary(): Expression {
        if (match(TokenType.BANG) || match(TokenType.MINUS)) {
            val operator = previous().getType()
            val right = parseUnary()
            return UnaryOp(operator, right)
        }
        return parsePrimary()
    }

    private fun parsePrimary(): Expression {
        if (match(TokenType.KEYWORD_TRUE)) { return BooleanLiteral(true) }
        if (match(TokenType.KEYWORD_FALSE)) { return BooleanLiteral(false) }
        if (match(TokenType.STRING_LITERAL)) { return StringLiteral(previous().getLiteral()) }
        if (match(TokenType.INTEGER_LITERAL)) { return IntegerLiteral(previous().getLiteral().toInt()) }
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            if (peek()?.getType() == TokenType.LEFT_PAREN) {
                return parseFunctionCall(FUNCTION.EXPRESSION) as FuncCallExpr
            } else if (peek()?.getType() == TokenType.LEFT_BRACKET) {
                return parseConstructorCall() as ConstructorCallExpr
            }
            match(TokenType.IDENTIFIER)
            return Variable(previous().getLiteral())
        }

        if (match(TokenType.LEFT_PAREN)) {
            val expr = parseExpression()

            if (currentToken.getType() != TokenType.RIGHT_PAREN) {
                println("Expected ')' at the end of expression")
                exitProcess(0)
            }
            consume()
            return Grouping(expr)
        }
        println("Invalid Expression")
        exitProcess(0)
    }
}