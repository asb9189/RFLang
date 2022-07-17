package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node

class Grouping(expression: Expression) : Node(), Expression {

    private val expression: Expression

    init {
        this.expression = expression
    }

    override fun eval(): Any {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}