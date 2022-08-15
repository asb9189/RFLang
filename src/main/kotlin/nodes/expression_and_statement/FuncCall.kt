package nodes.expression_and_statement

import evaluator.*
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node
import nodes.statements.Return
import runtime.Runtime

class FuncCall(private val functionName: String, private val arguments: List<Expression>): Node(), Expression, Statement {

    fun getFunctionName(): String {
        return functionName
    }

    fun getArguments(): List<Expression> {
        return arguments
    }

    override fun eval(): Value {
        return Evaluator.executeFuncCall(this)
    }

    override fun getType(): StatementType {
        return StatementType.FUNC_CALL_STMT
    }

    override fun toString(): String {
        return "[FuncCall]"
    }
}