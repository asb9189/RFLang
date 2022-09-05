package standard_lib.objects

import evaluator.Value
import nodes.interfaces.Expression

abstract class StandardLibObject: Object() {
    abstract override fun callMethod(methodName: String, arguments: List<Expression>): Value
}
