package nodes.expressions

import evaluator.Environment
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class IntegerLiteral (value: Int): Node(), Expression {

    private val value: Int

    init {
        this.value = value
    }

    override fun eval(): Pair<Any, ValueType> {
        return Pair(value, ValueType.INTEGER)
    }

    override fun toString(): String {
        return this.value.toString()
    }
}