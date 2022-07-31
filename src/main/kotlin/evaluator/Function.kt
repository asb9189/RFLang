package evaluator

enum class FunctionType {
    USER_DEFINED,
    STANDARD_LIB
}

interface Function {

    fun getFunctionName(): String

    fun getParams(): List<String>

    fun getFunctionType(): FunctionType
}