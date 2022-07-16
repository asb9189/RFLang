package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node


class BinOp (lhs: Expression, op: OPERATOR, rhs: Expression): Node(), Expression {

    private val lhs: Expression
    private val rhs: Expression
    private val op: OPERATOR

    companion object {
        enum class OPERATOR {
            PLUS,
            MINUS,
            MULTIPLY,
            DIVIDE
        }
    }


    init {
        this.lhs = lhs
        this.rhs = rhs
        this.op = op
    }

    override fun eval() {
        TODO("Not yet implemented")
    }

}