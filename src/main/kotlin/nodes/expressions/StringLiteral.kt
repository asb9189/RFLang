package nodes.expressions

import evaluator.Environment
import evaluator.Value
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class StringLiteral (value: String): Node(), Expression {

    private val value: String
    init {
        this.value = value
    }

    override fun eval(): Value {
        return Value(value, ValueType.STRING)
    }

    override fun toString(): String {
        return this.value
    }
}