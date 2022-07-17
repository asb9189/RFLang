package nodes.expressions

import evaluator.Environment
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node
import kotlin.system.exitProcess

class Variable(varName: String): Node(), Expression {

    private val varName: String

    init {
        this.varName = varName
    }

    override fun eval(env: Environment): Pair<Any, ValueType> {
        env.getVariable(varName)?.let {
            return Pair(it.getValue(), it.getType())
        } ?: run {
            println("Failed to get variable '$varName' in Variable.eval()")
            exitProcess(0)
        }
    }

    override fun toString(): String {
        return "[Variable] varName=$varName"
    }
}