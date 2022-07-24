package nodes.expressions

import evaluator.Environment
import evaluator.EnvironmentManager
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node
import runtime.Runtime
import java.util.UUID

class Variable(varName: String): Node(), Expression {

    private val varName: String
    private val id: UUID

    init {
        this.varName = varName
        this.id = Runtime.generateUUID()
    }

    fun getVarName(): String {
        return varName
    }

    fun getID(): UUID {
        return id
    }

    override fun eval(): Pair<Any, ValueType> {
        EnvironmentManager.getVariable(varName).let {
            return Pair(it.getValue(), it.getType())
        }
    }

    override fun toString(): String {
        return "[Variable] varName=$varName UUID=${id}"
    }
}