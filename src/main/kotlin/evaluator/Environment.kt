package evaluator

import nodes.interfaces.Statement
import kotlin.system.exitProcess

class Environment {

    private var variableCount: Int = 0
    private val varToInt: HashMap<String, Int> = HashMap()
    private val symbolTable: HashMap<Int, Value> = HashMap()
    private val functionTable: HashMap<String, Function> = HashMap()

    fun declareVariable(varName: String, value: Any, type: ValueType) {
        if (doesVariableExist(varName).not()) {
            varToInt[varName] = variableCount++
            varToInt[varName]?.let {
                symbolTable[it] = Value(value, type)
            } ?: run {
                println("unexpected error while creating '$varName'")
                exitProcess(0)
            }
        } else {
            println("Failed to create variable '$varName' as it already exists!")
            exitProcess(0)
        }
    }

    fun declareFunction(functionName: String, params: List<String>, body: List<Statement>) {
        if (doesFunctionExist(functionName).not()) {
            functionTable[functionName] = Function(functionName, params, body)
        } else {
            println("Failed to create function '$functionName' as it already exists!")
            exitProcess(0)
        }
    }

    fun updateVariable(varName: String, value: Any, type: ValueType) {
        if (doesVariableExist(varName)) {
            varToInt[varName]?.let {
                symbolTable[it] = Value(value, type)
            } ?: run {
                println("unexpected error while updating '$varName'")
                exitProcess(0)
            }
        } else {
            println("Failed to update variable '$varName' as it doesn't exists!")
            exitProcess(0)
        }
    }

    fun getVariable(varName: String): Value {
        if (doesVariableExist(varName)) {
            varToInt[varName]?.let { it ->
                symbolTable[it]?.let { value ->
                    return value
                } ?: run {
                    println("unexpected error while looking up variable '$varName'")
                    exitProcess(0)
                }
            } ?: run {
                println("unexpected error while looking up variable '$varName'")
                exitProcess(0)
            }
        } else {
            println("unexpected error while looking up variable '$varName'")
            exitProcess(0)
        }
    }

    fun getFunction(functionName: String): Function {
        if (doesFunctionExist(functionName)) {
            functionTable[functionName]?.let { fn ->
                return fn
            } ?: run {
                println("unexpected error while looking up function '$functionName'")
                exitProcess(0)
            }
        } else {
            println("unexpected error while looking up function '$functionName'")
            exitProcess(0)
        }
    }

    private fun doesVariableExist(varName: String): Boolean {
        varToInt[varName]?.let {
            return true
        } ?: return false
    }

    private fun doesFunctionExist(functionName: String): Boolean {
        functionTable[functionName]?.let {
            return true
        } ?: return false
    }

}