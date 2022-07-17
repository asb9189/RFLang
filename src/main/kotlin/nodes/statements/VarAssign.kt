package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class VarAssign (varName: String, expression: Expression): Node(), Statement {

    private val varName: String
    private val expression: Expression

    init {
        this.varName = varName
        this.expression = expression
    }

    fun getVarName(): String {
        return varName
    }

    fun getValue(): Expression {
        return expression
    }

    override fun getType(): StatementType {
        return StatementType.VAR_ASSIGN_STMT
    }

    override fun toString(): String {
        return "[VarAssign] varname=$varName value=$expression"
    }
}