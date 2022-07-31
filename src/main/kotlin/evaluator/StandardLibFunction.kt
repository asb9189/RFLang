package evaluator

import nodes.interfaces.Expression

class StandardLibFunction (
    private val functionName: String,
    private val params: List<String>,
    private val implementation: (List<Expression>) -> Value
)
: Function {

    fun run(arguments: List<Expression>): Value {
        return implementation(arguments)
    }

    override fun getFunctionName(): String {
        return functionName
    }

    override fun getParams(): List<String> {
        return params
    }

    override fun getFunctionType(): FunctionType {
        return FunctionType.STANDARD_LIB
    }
}