package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class Return(private val expression: Expression): Node(), Statement {

    fun getExpression(): Expression {
        return expression
    }

    override fun getType(): StatementType {
        return StatementType.RETURN_STMT
    }

    override fun toString(): String {
        return "[Return] expression=$expression"
    }
}