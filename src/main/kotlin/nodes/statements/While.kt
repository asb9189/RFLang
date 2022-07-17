package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class While(condition: Expression, body: List<Statement>): Node(), Statement {

    private val condition: Expression
    private val body: List<Statement>

    init {
        this.condition = condition
        this.body = body
    }

    override fun getType(): StatementType {
        return StatementType.WHILE_STMT
    }

    override fun toString(): String {
        return "[While] duration=$condition length=${body.size}"
    }
}