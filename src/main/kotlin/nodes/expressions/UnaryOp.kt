package nodes.expressions

import evaluator.Value
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node
import runtime.Runtime
import tokens.TokenType

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
                        Runtime.raiseError("invalid unary operation")
                    }
                    ValueType.STRING -> {
                        Runtime.raiseError("invalid unary operation")
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
                        Runtime.raiseError("invalid unary operation")
                    }
                    ValueType.BOOLEAN -> {
                        Runtime.raiseError("invalid unary operation")
                    }
                }
            }
            else -> {
                Runtime.raiseError("invalid unary operator")
            }
        }
        Runtime.raiseError("reached end of unary op conditionals")
    }

    override fun toString(): String {
        return "[UnaryOp] operator='$operator'"
    }
}