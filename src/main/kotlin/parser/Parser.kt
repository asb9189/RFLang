package parser

import nodes.*
import nodes.expression_and_statement.ConstructorCall
import nodes.expression_and_statement.FuncCall
import nodes.expression_and_statement.MethodCall
import nodes.expressions.*
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.statements.*
import tokens.Token
import tokens.TokenType
import runtime.Runtime

class Parser(tokenStream: List<Token>) {

    private var currentIndex = 0
    private var currentToken: Token
    private var parsingFunctionDefinition = false
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
            TokenType.KEYWORD_FUN -> {
                if (parsingFunctionDefinition) {
                    Runtime.raiseError("Cannot declare nested functions")
                }
                return parseFunctionDeclarationStmt()
            }
            TokenType.KEYWORD_FOR -> return parseForInStmt()
            TokenType.KEYWORD_RETURN -> return parseReturnStmt()
            TokenType.KEYWORD_BREAK -> return parseBreakStmt()
            TokenType.KEYWORD_END -> return parseEndStmt()
            TokenType.KEYWORD_IF -> return parseIfStmt()
            TokenType.IDENTIFIER -> {
                if (peek()?.getType() == TokenType.LEFT_PAREN) {
                    return parseFunctionCall() as FuncCall
                } else if (peek()?.getType() == TokenType.PERIOD) {
                    return parseMethodCall() as MethodCall
                }
                return parseVarAssignmentStmt()
            }
            else -> {
                val msg = "Invalid statement in parse stmt"
                Runtime.raiseError(msg)
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

    private fun parseForInStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_FOR)
        val localVar = matchAndConsume(TokenType.IDENTIFIER)

        matchAndConsume(TokenType.KEYWORD_IN)
        val expr = parseExpression()

        matchAndConsume(TokenType.LEFT_CURLY_BRACE)
        val stmts = parseBodyStmtList()
        matchAndConsume(TokenType.RIGHT_CURLY_BRACE)

        return ForIn(localVar.getLiteral(), expr, stmts)

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

            if (currentToken.getType() == TokenType.PERIOD) {
                val methodCalls = parseChainedMethodCall(constructorName)
                return ConstructorCall(constructorName, emptyList(), methodCalls)
            }
            return ConstructorCall(constructorName, emptyList(), emptyList())
        }

        val arguments = parseArguments(TokenType.RIGHT_BRACKET)
        if (currentToken.getType() == TokenType.PERIOD) {
            val methodCalls = parseChainedMethodCall(constructorName)
            return ConstructorCall(constructorName, arguments, methodCalls)
        }

        return ConstructorCall(constructorName, arguments, emptyList())
    }

    private fun parseMethodCall(): MethodCall {
        val objectName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
        matchAndConsume(TokenType.PERIOD)

        val methodName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
        matchAndConsume(TokenType.LEFT_PAREN)

        if (currentToken.getType() == TokenType.RIGHT_PAREN) {
            matchAndConsume(TokenType.RIGHT_PAREN)
            if (currentToken.getType() == TokenType.PERIOD) {
                val methodCalls = parseChainedMethodCall(objectName)
                return MethodCall(objectName, methodName, emptyList(), methodCalls)
            }

            return MethodCall(objectName, methodName, emptyList(), emptyList())

        }

        val arguments = parseArguments(TokenType.RIGHT_PAREN)
        if (currentToken.getType() == TokenType.PERIOD) {
            val methodCalls = parseChainedMethodCall(objectName)
            return MethodCall(objectName, methodName, arguments, methodCalls)
        }

        return MethodCall(objectName, methodName, arguments, emptyList())
    }

    private fun parseChainedMethodCall(objectName: String): List<MethodCall> {
        val methodCalls = mutableListOf<MethodCall>()
        while (currentToken.getType() == TokenType.PERIOD) {
            matchAndConsume(TokenType.PERIOD)
            val methodName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
            matchAndConsume(TokenType.LEFT_PAREN)

            if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                matchAndConsume(TokenType.RIGHT_PAREN)
                methodCalls.add(MethodCall(objectName, methodName, emptyList(), emptyList()))
            } else {
                val arguments = parseArguments(TokenType.RIGHT_PAREN)
                methodCalls.add(MethodCall(objectName, methodName, arguments, emptyList()))
            }
        }
        return methodCalls
    }

    private fun parseFunctionCall(): Any {
        val functionName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
        matchAndConsume(TokenType.LEFT_PAREN)

        if (currentToken.getType() == TokenType.RIGHT_PAREN) {
            matchAndConsume(TokenType.RIGHT_PAREN)
            return FuncCall(functionName, emptyList())
        }

        val arguments = parseArguments(TokenType.RIGHT_PAREN)
        return FuncCall(functionName, arguments)
    }

    private fun parseArguments(terminatingToken: TokenType): List<Expression> {
        var args = emptyList<Expression>()
        args = args.plus(parseExpression())

        while (currentToken.getType() == TokenType.COMMA) {
            matchAndConsume(TokenType.COMMA)
            args = args.plus(parseExpression())
        }
        matchAndConsume(terminatingToken)
        return args
    }

    private fun parseFunctionDeclarationStmt(): Statement {
        parsingFunctionDefinition = true
        matchAndConsume(TokenType.KEYWORD_FUN).getLiteral()
        val functionName = matchAndConsume(TokenType.IDENTIFIER).getLiteral()
        matchAndConsume(TokenType.LEFT_PAREN)

        if (currentToken.getType() == TokenType.RIGHT_PAREN) {
            matchAndConsume(TokenType.RIGHT_PAREN)
            matchAndConsume(TokenType.LEFT_CURLY_BRACE)

            val stmts = parseBodyStmtList()
            matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
            parsingFunctionDefinition = false
            return FuncDef(functionName, emptyList(), stmts)
        }

        val params = parseParams()
        matchAndConsume(TokenType.RIGHT_PAREN)
        matchAndConsume(TokenType.LEFT_CURLY_BRACE)
        val stmts = parseBodyStmtList()
        matchAndConsume(TokenType.RIGHT_CURLY_BRACE)
        parsingFunctionDefinition = false
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

    private fun parseBreakStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_BREAK)
        return Break()
    }

    private fun parseEndStmt(): Statement {
        matchAndConsume(TokenType.KEYWORD_END)
        return End()
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
            val msg = "Expected to match token '$tokenType' but got '${currentToken.getType()}'"
            Runtime.raiseError(msg)
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
                return parseFunctionCall() as FuncCall
            } else if (peek()?.getType() == TokenType.LEFT_BRACKET) {
                return parseConstructorCall() as ConstructorCall
            } else if (peek()?.getType() == TokenType.PERIOD) {
                return parseMethodCall() as MethodCall
            }
            match(TokenType.IDENTIFIER)
            return Variable(previous().getLiteral())
        }

        if (match(TokenType.LEFT_PAREN)) {
            val expr = parseExpression()

            if (currentToken.getType() != TokenType.RIGHT_PAREN) {
                val msg = "Expected ')' at the end of expression"
                Runtime.raiseError(msg)
            }
            consume()
            return Grouping(expr)
        }
        val msg = "Invalid Expression"
        Runtime.raiseError(msg)
    }
}