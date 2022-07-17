package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node

class IntegerLiteral (value: Int): Node(), Expression {

    private val value: Int

    init {
        this.value = value
    }

    override fun eval(): Int {
        return value
    }

    override fun toString(): String {
        return this.value.toString()
    }
}