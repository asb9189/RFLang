package nodes.interfaces

import evaluator.Value

interface Expression {
    fun eval(): Value
}