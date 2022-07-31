package nodes.expressions

import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node

class ConstructorCallExpr(private val constructorName: String, private val expressions: List<Expression>): Node(), Expression {

    fun getConstructorName(): String {
        return constructorName
    }

    override fun eval(): Pair<Any, ValueType> {
        throw NotImplementedError("See ConstructorCallExpr")
    }

    override fun toString(): String {
        return "[ConstructorCallExpr] constructorName=$constructorName"
    }
}