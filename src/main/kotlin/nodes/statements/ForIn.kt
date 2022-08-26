package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class ForIn (
    private val localVar: String,
    private val expression: Expression,
    private val stmts: List<Statement>
): Node(), Statement {

    fun getLocalVar(): String {
        return localVar
    }

    fun getExpression(): Expression {
        return expression
    }

    fun getStmts(): List<Statement> {
        return stmts
    }

    override fun getType(): StatementType {
        return StatementType.FOR_IN_STMT
    }

    override fun toString(): String {
        return "[For In]"
    }
}