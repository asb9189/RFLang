package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class MethodCallStmt(
    private val objectName: String,
    private val methodName: String,
    private val arguments: List<Expression>
): Node(), Statement {

    fun getObjectName(): String {
        return objectName
    }

    fun getMethodName(): String {
        return methodName
    }

    fun getArguments(): List<Expression> {
        return arguments
    }

    override fun getType(): StatementType {
        return StatementType.METHOD_CALL_STMT
    }

    override fun toString(): String {
        return "[MethodCallStmt]"
    }
}