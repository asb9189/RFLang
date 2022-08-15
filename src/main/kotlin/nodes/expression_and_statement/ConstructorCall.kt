package nodes.expression_and_statement

import evaluator.Evaluator
import evaluator.Value
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class ConstructorCall(private val constructorName: String, private val expressions: List<Expression>): Node(), Expression, Statement {

    fun getConstructorName(): String {
        return constructorName
    }

    fun getExpressions(): List<Expression> {
        return expressions
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