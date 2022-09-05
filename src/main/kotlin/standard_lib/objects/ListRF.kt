package standard_lib.objects

import evaluator.Value
import evaluator.ValueType
import nodes.interfaces.Expression
import runtime.Runtime
import standard_lib.interfaces.iterable

class ListRF: StandardLibObject(), iterable {

    enum class METHODS(val literal: String) {
        ADD("add"),
        REMOVE("remove"),
        REMOVE_ALL("removeAll"),
        LENGTH("Length"),
        IS_EMPTY("isEmpty"),
        CONTAINS("contains")
    }

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
        return Value(Value.Companion.NULL(), ValueType.NULL)
    }

    fun remove(value: Value): Value {
        if (list.isEmpty().not()) {
            if (value.getType() != type) {
                Runtime.raiseError("Cannot remove ${value.getType()} from List of type $type")
            }
            list.remove(value.getValue())
        }
        return Value(Value.Companion.NULL(), ValueType.NULL)
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
        return Value(Value.Companion.NULL(), ValueType.NULL)
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

    fun isEmptyKotlin(): Boolean {
        return list.isEmpty()
    }

    fun getListKotlin(): MutableList<Any> {
        return list
    }

    override fun callMethod(methodName: String, arguments: List<Expression>): Value {
        when (methodName) {
            METHODS.ADD.literal -> {
                if (arguments.size != 1) {
                    Runtime.raiseError("add expects a single argument")
                }
                return this.add(arguments[0].eval())
            }
            METHODS.REMOVE.literal -> {
                return this.remove(arguments[0].eval())
            }
            METHODS.REMOVE_ALL.literal -> {
                return this.removeAll(arguments[0].eval())
            }
            METHODS.LENGTH.literal -> {
                return this.length()
            }
            METHODS.IS_EMPTY.literal -> {
                return this.isEmpty()
            }
            METHODS.CONTAINS.literal -> {
                return this.contains(arguments[0].eval())
            }
            else -> Runtime.raiseError("List does not have method '${methodName}'")
        }
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