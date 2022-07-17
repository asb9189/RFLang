package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class FuncCall(function: String, arguments: List<Expression>): Node(), Statement {

    private val function: String
    private val arguments: List<Expression>

    init {
        this.function = function
        this.arguments = arguments
    }

    override fun getType(): StatementType {
        return StatementType.FUNC_CALL_STMT
    }

    override fun toString(): String {
        return "[FuncCall]"
    }
}