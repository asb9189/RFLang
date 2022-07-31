package evaluator

import nodes.interfaces.Statement

class UserDefinedFunction (
    private val functionName: String,
    private val params: List<String>,
    private val body: List<Statement> )
: Function {

    fun getBody(): List<Statement> {
        return body
    }

    override fun getFunctionName(): String {
        return functionName
    }

    override fun getParams(): List<String> {
        return params
    }

    override fun getFunctionType(): FunctionType {
        return FunctionType.USER_DEFINED
    }
}