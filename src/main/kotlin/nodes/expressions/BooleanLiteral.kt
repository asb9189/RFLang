package nodes.expressions

import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class BooleanLiteral (value: Boolean): Node(), Expression {

    private val value: Boolean
    init {
        this.value = value
    }

    override fun eval(): Pair<Any, ValueType> {
        return Pair(value, ValueType.BOOLEAN)
    }

    override fun toString(): String {
        return this.value.toString()
    }
}