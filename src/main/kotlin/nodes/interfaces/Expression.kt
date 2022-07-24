package nodes.interfaces

import evaluator.ValueType

interface Expression {
    fun eval(): Pair<Any, ValueType>
}