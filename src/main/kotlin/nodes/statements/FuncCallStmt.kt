package nodes.statements

import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class FuncCallStmt(functionName: String, arguments: List<Expression>): Node(), Statement {

    private val functionName: String
    private val arguments: List<Expression>

    init {
        this.functionName = functionName
        this.arguments = arguments
    }

    fun getFunctionName(): String {
        return functionName
    }

    fun getArguments(): List<Expression> {
        return arguments
    }

    override fun getType(): StatementType {
        return StatementType.FUNC_CALL_STMT
    }

    override fun toString(): String {
        return "[FuncCall]"
    }
}