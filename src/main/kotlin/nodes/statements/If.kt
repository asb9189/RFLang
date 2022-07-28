package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class If (
    private val ifCondition: Expression,
    private val ifStmts: List<Statement>,
    private val elseIfList: List<ElseIf>,
    private val elseStmts: List<Statement>
): Node(), Statement {

    fun getIfCondition(): Expression {
        return ifCondition
    }

    fun getIfStmts(): List<Statement> {
        return ifStmts
    }

    fun getElseIfList(): List<ElseIf> {
        return elseIfList
    }

    fun getElseStmts(): List<Statement> {
        return elseStmts
    }

    override fun getType(): StatementType {
        return StatementType.IF_STMT
    }

    override fun toString(): String {
        return "[If]"
    }
}