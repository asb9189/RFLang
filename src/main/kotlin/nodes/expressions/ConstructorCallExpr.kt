package nodes.expressions

import evaluator.EnvironmentManager
import evaluator.Value
import standard_lib.objects.ListRF
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node
import runtime.Runtime

class ConstructorCallExpr(private val constructorName: String, private val expressions: List<Expression>): Node(), Expression {

    fun getConstructorName(): String {
        return constructorName
    }

    override fun eval(): Value {
        val value = EnvironmentManager.createObject(constructorName)
        return Value(value.getValue(), value.getType())
    }

    override fun toString(): String {
        return "[ConstructorCallExpr] constructorName=$constructorName"
    }
}