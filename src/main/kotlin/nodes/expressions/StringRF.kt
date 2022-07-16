package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node

class StringRF (value: String): Node(), Expression {

    private val value: String
    init {
        this.value = value
    }

    override fun eval(): String {
        return value
    }

    override fun toString(): String {
        return this.value
    }
}