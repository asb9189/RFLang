package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node
import tokens.TokenType

class UnaryOp(operator: TokenType, expression: Expression): Node(), Expression {

    private val operator: TokenType
    private val expression: Expression

    init {
        this.operator = operator
        this.expression = expression
    }

    override fun eval(): Any {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}