package nodes.expressions

import evaluator.Value
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class IntegerLiteral (value: Int): Node(), Expression {

    private val value: Int

    init {
        this.value = value
    }

    override fun eval(): Value {
        return Value(value, ValueType.INTEGER)
    }

    override fun toString(): String {
        return this.value.toString()
    }
}