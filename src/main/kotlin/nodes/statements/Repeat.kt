package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class Repeat(expression: Expression, body: List<Statement>): Node(), Statement {

    private val expression: Expression
    private val body: List<Statement>

    init {
        this.expression = expression
        this.body = body
    }

    fun getCondition(): Expression {
        return expression
    }

    fun getBody(): List<Statement> {
        return body
    }

    override fun getType(): StatementType {
        return StatementType.REPEAT_STMT
    }

    override fun toString(): String {
        return "[Repeat] duration=$expression length=${body.size}"
    }
}