package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class VarAssign (varName: String, value: Expression): Node(), Statement {

    private val varName: String
    private val value: Expression

    init {
        this.varName = varName
        this.value = value
    }

    override fun getType(): StatementType {
        return StatementType.VAR_ASSIGN_STMT
    }

    override fun toString(): String {
        return "[VarAssign] varname=$varName value=$value"
    }
}