package nodes.expressions

import evaluator.Environment
import evaluator.Value
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class BooleanLiteral (value: Boolean): Node(), Expression {

    private val value: Boolean
    init {
        this.value = value
    }

    override fun eval(): Value {
        return Value(value, ValueType.BOOLEAN)
    }

    override fun toString(): String {
        return this.value.toString()
    }
}