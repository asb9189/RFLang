package standard_lib.objects

import evaluator.Value
import evaluator.ValueType
import runtime.Runtime
import standard_lib.interfaces.iterable
import kotlin.collections.List

class ListRF(): Object(), iterable {

    private var list: List<Any> = emptyList()
    private var type: ValueType = ValueType.NULL

    fun add(value: Value) {
        if (type == ValueType.NULL) {
            type = value.getType()
        }

        if (value.getType() != type) {
            Runtime.raiseError("Cannot add type ${value.getType()} to List of type $type")
        } else if (value.getType() == ValueType.NULL) {
            Runtime.raiseError("Cannot add type ${ValueType.NULL} to List")
        }
        list = list.plus(value.getValue())
    }

    fun getType(): ValueType {
        return type
    }

    override fun type(): TYPE {
        return TYPE.LIST
    }

    override fun toString(): String {

        if (list.isEmpty()) {
            return "[]"
        }

        var result = "["

        list.forEachIndexed { index, any ->
            result += if (index < list.size - 1) {
                "$any, "
            } else {
                "$any]"
            }
        }

        return result
    }
}