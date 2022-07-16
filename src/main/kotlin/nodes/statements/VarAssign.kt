package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.root.Node

class VarAssign (varname: String, value: Expression): Node(), Statement {

    private val varname: String
    private val value: Expression

    init {
        this.varname = varname
        this.value = value
    }

    override fun toString(): String {
        return "[VarAssign] varname=$varname value=$value"
    }
}