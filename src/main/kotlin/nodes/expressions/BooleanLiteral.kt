package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node

class BooleanLiteral (value: Boolean): Node(), Expression {

    private val value: Boolean
    init {
        this.value = value
    }

    override fun eval(): Boolean {
        return value
    }

    override fun toString(): String {
        return this.value.toString()
    }
}