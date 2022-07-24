package evaluator

import nodes.interfaces.Statement

class Function(functionName: String, params: List<String>, body: List<Statement>) {

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

}