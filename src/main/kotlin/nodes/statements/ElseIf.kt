package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class ElseIf (
    private val condition: Expression,
    private val stmts: List<Statement>
): Node(), Statement {

    fun getCondition(): Expression {
        return condition
    }

    fun getStmts(): List<Statement> {
        return stmts
    }

    override fun getType(): StatementType {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "[ElseIf]"
    }
}