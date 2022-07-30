package nodes.expressions

import evaluator.EnvironmentManager
import evaluator.Evaluator
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.interfaces.StatementType
import nodes.root.Node
import nodes.statements.Return
import runtime.Runtime

class FuncCallExpr(private val functionName: String, private val arguments: List<Expression>): Node(), Expression {

    fun getFunctionName(): String {
        return functionName
    }

    fun getArguments(): List<Expression> {
        return arguments
    }

    fun getNumberOfArguments(): Int {
        return arguments.size
    }

    override fun eval(): Pair<Any, ValueType> {

        val function = EnvironmentManager.getFunction(functionName)
        val functionParams = function.getParams()
        val functionBody = function.getBody()

        if (arguments.size != functionParams.size) {
            Runtime.raiseError(
                "Function '$functionName' expected ${functionParams.size} argument(s) but received" +
                        " ${arguments.size} argument(s) instead")
        }

        EnvironmentManager.pushFunctionEnvironment()
        arguments.forEachIndexed { index, expression ->
            val (value, type) = expression.eval()
            EnvironmentManager.declareVariable(
                functionParams[index],
                value,
                type
            )
        }

        for (stmt in functionBody) {
            if (stmt.getType() == StatementType.RETURN_STMT) {
                val (rvalue, rtype) = Evaluator.executeReturnStmt(stmt as Return)

                EnvironmentManager.popFunctionEnvironment()
                return Pair(rvalue, rtype)
            }
            Evaluator.executeStatement(stmt)
        }

        EnvironmentManager.popFunctionEnvironment()
        return Pair(-1, ValueType.NULL)
    }

    override fun toString(): String {
        return "[FuncCall]"
    }
}