package standard_lib.objects

import evaluator.Value
import nodes.interfaces.Expression

enum class ObjectType {
    USER_DEFINED,
    STANDARD_LIB
}

abstract class Object {
    abstract fun name(): String
    abstract fun type(): ObjectType
    abstract fun callMethod(methodName: String, arguments: List<Expression>): Value
    abstract override fun toString(): String
}