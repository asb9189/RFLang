package nodes.expressions

import evaluator.Environment
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class Grouping(expression: Expression) : Node(), Expression {

    private val expression: Expression

    init {
        this.expression = expression
    }

    override fun eval(env: Environment): Pair<Any, ValueType> {
        val (value, type) = expression.eval(env)
        return Pair(value, type)
    }

    override fun toString(): String {
        return "(${expression})"
    }
}