package nodes.expressions

import evaluator.*
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

    override fun eval(): Value {

        var function = EnvironmentManager.getFunction(functionName)
        when (function.getFunctionType()) {
            FunctionType.USER_DEFINED -> {
                function = function as UserDefinedFunction
                val functionParams = function.getParams()
                val functionBody = function.getBody()

                if (arguments.size != functionParams.size) {
                    Runtime.raiseError(
                        "Function '$functionName' expected ${functionParams.size} argument(s) but received" +
                                " ${arguments.size} argument(s) instead")
                }

                EnvironmentManager.pushFunctionEnvironment()
                arguments.forEachIndexed { index, expression ->
                    val value = expression.eval()
                    EnvironmentManager.declareVariable(
                        functionParams[index],
                        value.getValue(),
                        value.getType()
                    )
                }

                for (stmt in functionBody) {
                    if (stmt.getType() == StatementType.RETURN_STMT) {
                        val value = Evaluator.executeReturnStmt(stmt as Return)

                        EnvironmentManager.popFunctionEnvironment()
                        return value
                    } else if (stmt.getType() == StatementType.BREAK_STMT) {
                        EnvironmentManager.popFunctionEnvironment()
                        return Value(-1, ValueType.NULL)
                    }
                    Evaluator.executeStatement(stmt)
                }

                EnvironmentManager.popFunctionEnvironment()
                return Value(-1, ValueType.NULL)
            }
            FunctionType.STANDARD_LIB -> {
                function = function as StandardLibFunction
                val functionParams = function.getParams()
                if (arguments.size != functionParams.size) {
                    Runtime.raiseError(
                        "Function '$functionName' expected ${functionParams.size} argument(s) but received" +
                                " ${arguments.size} argument(s) instead")
                }
                val value = function.run(arguments)
                return Value(value.getValue(), value.getType())
            }
        }
    }

    override fun toString(): String {
        return "[FuncCall]"
    }
}