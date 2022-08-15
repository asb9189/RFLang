package nodes.expression_and_statement

import evaluator.EnvironmentManager
import evaluator.Evaluator
import evaluator.Value
import standard_lib.objects.ListRF
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node
import runtime.Runtime

class ConstructorCall(private val constructorName: String, private val expressions: List<Expression>): Node(), Expression, Statement {

    fun getConstructorName(): String {
        return constructorName
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