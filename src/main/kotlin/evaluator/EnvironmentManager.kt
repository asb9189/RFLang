package evaluator

import nodes.interfaces.Statement
import runtime.Runtime
import standard_lib.StandardLibBuilder
import java.util.*
import kotlin.collections.HashMap

class EnvironmentManager {
    companion object {

        private val mainEnv: Environment = Environment()
        private val functionEnvStack: Stack<Environment> = Stack()
        private val functionTable: HashMap<String, Function> = HashMap()

        init {
            val standardLibFunctions = StandardLibBuilder.buildStandardLib()
            for (func in standardLibFunctions) {
                functionTable[func.getFunctionName()] = func
            }
        }

        fun declareVariable(symbol: String, value: Any, type: ValueType) {
            if (functionEnvStack.empty()) {
                mainEnv.declareVariable(symbol, Value(value, type))
            } else {
                functionEnvStack.peek().declareVariable(symbol, Value(value, type))
            }
        }

        fun getVariable(symbol: String): Value {
            if (functionEnvStack.empty()) {
                val (result, isSuccess) = mainEnv.getVariable(symbol)
                if (isSuccess) { return result as Value }
                else { Runtime.raiseError("Failed to get variable '$symbol'") }
            } else {
                val (result, isSuccess) = functionEnvStack.peek().getVariable(symbol)
                if (isSuccess) { return result as Value }
                else {
                    val (result2, isSuccess2) = mainEnv.getVariable(symbol)
                    if (isSuccess2) { return result2 as Value }
                    else { Runtime.raiseError("Failed to get variable '$symbol'") }
                }
            }
        }

        fun updateVariable(symbol: String, value: Any, type: ValueType) {
            if (functionEnvStack.empty()) {
                mainEnv.updateVariable(symbol, Value(value, type))
            } else {
                functionEnvStack.peek().updateVariable(symbol, Value(value, type))
            }
        }

        fun declareFunction(functionName: String, params: List<String>, body: List<Statement>) {
            if (doesFunctionExist(functionName).not()) {
                functionTable[functionName] = UserDefinedFunction(functionName, params, body)
            } else {
                Runtime.raiseError("Failed to declare new function")
            }
        }

        fun getFunction(functionName: String): Function {
            if (doesFunctionExist(functionName)) {
                functionTable[functionName]?.let {
                    return it
                } ?: run {
                    Runtime.raiseError("Function '$functionName' is not defined")
                }
            } else {
                Runtime.raiseError("Function '$functionName' is not defined")
            }
        }

        fun pushFunctionEnvironment() {
            functionEnvStack.push(Environment())
        }

        fun popFunctionEnvironment() {
            functionEnvStack.pop()
        }

        fun isFunctionEnvironmentEmpty(): Boolean {
            return functionEnvStack.isEmpty()
        }

        private fun doesFunctionExist(functionName: String): Boolean {
            functionTable[functionName]?.let {
                return true
            } ?: return false
        }
    }
}