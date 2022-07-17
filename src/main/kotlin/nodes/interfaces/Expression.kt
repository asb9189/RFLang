package nodes.interfaces

import evaluator.Environment
import evaluator.ValueType

interface Expression {
    fun eval(env: Environment): Pair<Any, ValueType>
}