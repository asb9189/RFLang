package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class Return(expression: Expression): Node(), Statement {

    private val expression: Expression

    init {
        this.expression = expression
    }

    override fun getType(): StatementType {
        return StatementType.RETURN_STMT
    }

    override fun toString(): String {
        return "[Return] expression=$expression"
    }
}