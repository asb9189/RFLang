package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class Repeat(duration: Expression, body: List<Statement>): Node(), Statement {

    private val duration: Expression
    private val body: List<Statement>

    init {
        this.duration = duration
        this.body = body
    }

    override fun getType(): StatementType {
        return StatementType.REPEAT_STMT
    }

    override fun toString(): String {
        return "[Repeat] duration=$duration length=${body.size}"
    }
}