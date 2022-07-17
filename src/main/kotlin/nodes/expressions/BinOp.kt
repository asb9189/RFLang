package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node
import tokens.TokenType


class BinOp (lhs: Expression, operator: TokenType, rhs: Expression): Node(), Expression {

    private val lhs: Expression
    private val rhs: Expression
    private val operator: TokenType

    init {
        this.lhs = lhs
        this.rhs = rhs
        this.operator = operator
    }

    override fun eval() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "BinOp"
    }

}