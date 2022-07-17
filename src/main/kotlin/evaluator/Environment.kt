package evaluator

import kotlin.system.exitProcess

class Environment {

    private var variableCount: Int = 0
    private val varToInt: HashMap<String, Int> = HashMap()
    private val symbolTable: HashMap<Int, Value> = HashMap()

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

    fun getVariable(varName: String): Value? {
        varToInt[varName]?.let {
            return symbolTable[it]
        } ?: run {
            println("unexpected error while creating '$varName'")
            exitProcess(0)
        }
    }

    private fun doesVariableExist(varName: String): Boolean {
        varToInt[varName]?.let {
            return true
        } ?: return false
    }

}