package standard_lib.objects

import evaluator.Value
import nodes.interfaces.Expression

class UserDefinedObject: Object() {
    override fun name(): String {
        TODO("Not yet implemented")
    }

    override fun type(): ObjectType {
        TODO("Not yet implemented")
    }

    override fun callMethod(methodName: String, arguments: List<Expression>): Value {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}