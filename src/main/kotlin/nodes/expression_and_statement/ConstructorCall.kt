package nodes.expression_and_statement

import evaluator.Evaluator
import evaluator.Value
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class ConstructorCall(
    private val constructorName: String,
    private val expressions: List<Expression>,
    private val chainedMethodCalls: List<MethodCall>
): Node(), Expression, Statement {

    fun getConstructorName(): String {
        return constructorName
    }

    fun getExpressions(): List<Expression> {
        return expressions
    }

    fun hasChainedMethodCalls(): Boolean {
        return chainedMethodCalls.isNotEmpty()
    }

    fun getChainedMethodCalls(): List<MethodCall> {
        return chainedMethodCalls
    }

    override fun eval(): Value {
        return Evaluator.executeConstructorCall(this)
    }

    override fun getType(): StatementType {
        return StatementType.CONSTRUCTOR_CALL_STMT
    }

    override fun toString(): String {
        return "[ConstructorCallExpr] constructorName=$constructorName"
    }
}