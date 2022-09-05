package nodes.expression_and_statement

import evaluator.*
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node
import runtime.Runtime
import standard_lib.objects.ListRF
import standard_lib.objects.Object
import standard_lib.objects.ObjectType

class MethodCall(
    private val objectName: String,
    private val methodName: String,
    private val arguments: List<Expression>,
    private val chainedMethodCalls: List<MethodCall>
): Node(), Expression, Statement {

    fun getObjectName(): String {
        return objectName
    }

    fun getMethodName(): String {
        return methodName
    }

    fun getArguments(): List<Expression> {
        return arguments
    }

    fun isChainedMethodCall(): Boolean {
        return chainedMethodCalls.isNotEmpty()
    }

    fun getChainedMethodCalls(): List<MethodCall> {
        return chainedMethodCalls
    }

    override fun eval(): Value {
        return Evaluator.executeMethodCall(this)
    }

    override fun getType(): StatementType {
        return StatementType.METHOD_CALL_STMT
    }

    override fun toString(): String {
        return "[MethodCallExpr]"
    }
}