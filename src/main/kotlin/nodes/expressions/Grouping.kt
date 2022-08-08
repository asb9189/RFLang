package nodes.expressions

import evaluator.Environment
import evaluator.Value
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class Grouping(expression: Expression) : Node(), Expression {

    private val expression: Expression

    init {
        this.expression = expression
    }

    override fun eval(): Value {
        return expression.eval()
    }

    override fun toString(): String {
        return "(${expression})"
    }
}