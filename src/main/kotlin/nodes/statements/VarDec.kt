package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class VarDec (varName: String, value: Expression): Node(), Statement {

    private val varName: String
    private val value: Expression

    init {
        this.varName = varName
        this.value = value
    }

    fun getVarName(): String {
        return varName
    }

    fun getValue(): Expression {
        return value
    }

    override fun getType(): StatementType {
        return StatementType.VAR_DEC_STMT
    }

    override fun toString(): String {
        return "[VarDec] varname=$varName value=$value"
    }
}