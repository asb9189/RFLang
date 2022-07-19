package nodes.statements

import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class FuncDef(functionName: String, params: List<String>, body: List<Statement>): Node(), Statement {

    private val functionName: String
    private val params: List<String>
    private val body: List<Statement>

    init {
        this.functionName = functionName
        this.params = params
        this.body = body
    }

    fun getFunctionName(): String {
        return functionName
    }

    fun getParams(): List<String> {
        return params
    }

    fun getBody(): List<Statement> {
        return body
    }

    override fun getType(): StatementType {
        return StatementType.FUNC_DEF_STMT
    }

    override fun toString(): String {
        return "[FuncDef]"
    }
}