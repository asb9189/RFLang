package standard_lib.objects

import evaluator.Value
import evaluator.ValueType
import runtime.Runtime
import standard_lib.interfaces.iterable

class ListRF: Object(), iterable {

    private var list: MutableList<Any> = mutableListOf()
    private var type: ValueType = ValueType.NULL

    fun add(value: Value): Value {
        if (type == ValueType.NULL) {
            type = value.getType()
        }

        if (value.getType() != type) {
            Runtime.raiseError("Cannot add type ${value.getType()} to List of type $type")
        } else if (value.getType() == ValueType.NULL) {
            Runtime.raiseError("Cannot add type ${ValueType.NULL} to List")
        }
        list.add(value.getValue())
        return Value(-1, ValueType.NULL)
    }

    fun remove(value: Value): Value {
        if (list.isEmpty().not()) {
            if (value.getType() != type) {
                Runtime.raiseError("Cannot remove ${value.getType()} from List of type $type")
            }
            list.remove(value.getValue())
        }
        return Value(-1, ValueType.NULL)
    }

    fun removeAll(value: Value): Value {
        if (list.isEmpty().not()) {
            if (value.getType() != type) {
                Runtime.raiseError("Cannot remove ${value.getType()} from List of type $type")
            }
            list.removeIf {
                value.getValue() == it
            }
        }
        return Value(-1, ValueType.NULL)
    }

    fun contains(value: Value): Value {
        if (list.isEmpty().not()) {
            if (value.getType() != type) {
                Runtime.raiseError("Cannot check for type ${value.getType()} from List of type $type")
            }
            return Value(list.contains(value.getValue()), ValueType.BOOLEAN)
        }
        return Value(false, ValueType.BOOLEAN)
    }

    fun length(): Value {
        return Value(list.size, ValueType.INTEGER)
    }

    fun isEmpty(): Value {
        return Value(list.isEmpty(), ValueType.BOOLEAN)
    }

    fun getType(): ValueType {
        return type
    }

    override fun name(): String {
        return "List"
    }

    override fun type(): ObjectType {
        return ObjectType.STANDARD_LIB
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