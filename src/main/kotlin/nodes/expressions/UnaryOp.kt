package nodes.expressions

import evaluator.Value
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

    override fun eval(): Value {
        val value = expression.eval()
        when (operator) {
            TokenType.BANG -> {
                when (value.getType()) {
                    ValueType.INTEGER -> {
                        println("invalid unary operation")
                        exitProcess(0)
                    }
                    ValueType.STRING -> {
                        println("invalid unary operation")
                        exitProcess(0)
                    }
                    ValueType.BOOLEAN -> {
                        if (value.getValue() == true) {
                            return Value(false, ValueType.BOOLEAN)
                        }
                    }
                }
            }
            TokenType.MINUS -> {
                when (value.getType()) {
                    ValueType.INTEGER -> {
                        return Value( (value.getValue() as Int) * -1, ValueType.INTEGER)
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
        return "[UnaryOp] operator='$operator'"
    }
}