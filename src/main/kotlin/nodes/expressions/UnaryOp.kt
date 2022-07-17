package nodes.expressions

import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node
import tokens.TokenType
import kotlin.system.exitProcess

class UnaryOp(operator: TokenType, expression: Expression): Node(), Expression {

    private val operator: TokenType
    private val expression: Expression

    init {
        this.operator = operator
        this.expression = expression
    }

    override fun eval(): Pair<Any, ValueType> {
        val (value, type) = expression.eval()
        when (operator) {
            TokenType.BANG -> {
                when (type) {
                    ValueType.INTEGER -> {
                        println("invalid unary operation")
                        exitProcess(0)
                    }
                    ValueType.STRING -> {
                        println("invalid unary operation")
                        exitProcess(0)
                    }
                    ValueType.BOOLEAN -> {
                        if (value == true) {
                            return Pair(false, type)
                        }
                    }
                }
            }
            TokenType.MINUS -> {
                when (type) {
                    ValueType.INTEGER -> {
                        return Pair( (value as Int) * -1, type)
                    }
                    ValueType.STRING -> {
                        println("invalid unary operation")
                        exitProcess(0)
                    }
                    ValueType.BOOLEAN -> {
                        println("invalid unary operation")
                        exitProcess(0)
                    }
                }
            }
            else -> {
                println("invalid unary operator")
                exitProcess(0)
            }
        }
        println("reached end of unary op conditionals")
        exitProcess(0)
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}