package evaluator

import runtime.Runtime

class Environment {


    private val symbolTable: HashMap<String, Value> = HashMap()

    fun getVariable(symbol: String): Pair<Any?, Boolean> {
        symbolTable[symbol]?.let {
            return Pair(it, true)
        } ?: run {
            return Pair(null, false)
        }
    }

    fun declareVariable(symbol: String, value: Value) {
        if (doesVariableExist(symbol).not()) {
            symbolTable[symbol] = value
        } else {
            Runtime.raiseError("Failed to declare variable")
        }
    }

    fun updateVariable(symbol: String, value: Value) {
        if (doesVariableExist(symbol)) {
            symbolTable[symbol] = value
        } else {
            Runtime.raiseError("Failed to update variable '$symbol'")
        }
    }

    private fun doesVariableExist(symbol: String): Boolean {
        symbolTable[symbol]?.let {
            return true
        } ?: return false
    }
}