package nodes.expressions

import evaluator.*
import nodes.interfaces.Expression
import nodes.root.Node

class MethodCallExpr(
    private val objectName: String,
    private val methodName: String,
    private val arguments: List<Expression>
): Node(), Expression {

    fun getObjectName(): String {
        return objectName
    }

    fun getMethodName(): String {
        return methodName
    }

    fun getArguments(): List<Expression> {
        return arguments
    }

    fun getNumberOfArguments(): Int {
        return arguments.size
    }

    override fun eval(): Pair<Any, ValueType> {
        throw NotImplementedError("Method call 'eval()' has not yet been implemented")
    }

    override fun toString(): String {
        return "[MethodCallExpr]"
    }
}