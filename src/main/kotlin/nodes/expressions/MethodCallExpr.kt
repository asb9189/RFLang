package nodes.expressions

import evaluator.*
import nodes.interfaces.Expression
import nodes.root.Node
import runtime.Runtime
import standard_lib.objects.ListRF
import standard_lib.objects.Object
import standard_lib.objects.ObjectType

class MethodCallExpr(
    private val objectName: String,
    private val methodName: String,
    private val arguments: List<Expression>
): Node(), Expression {

    fun getObjectName(): String {
        return objectName
    }

    fun getMethodName(): String {
        return methodName
    }

    fun getArguments(): List<Expression> {
        return arguments
    }

    fun getNumberOfArguments(): Int {
        return arguments.size
    }

    override fun eval(): Pair<Any, ValueType> {
        var value = EnvironmentManager.getVariable(objectName)

        if (value.getType() != ValueType.OBJECT) {
            Runtime.raiseError("${value.getType()} does not support method calls")
        }

        var obj = value.getValue() as Object
        when (obj.type()) {
            ObjectType.USER_DEFINED -> Runtime.raiseError("User defined objects not yet implemented")
            ObjectType.STANDARD_LIB -> {
                when (obj.name()) {
                    "List" -> {
                        obj = obj as ListRF
                        when (methodName) {
                            "add" -> {
                                if (arguments.size != 1) {
                                    Runtime.raiseError("add expects a single argument")
                                }
                                val v = arguments[0].eval()
                                val pair = obj.add(Value(v.first, v.second))
                                return Pair(pair.getValue(), pair.getType())
                            }
                            "remove" -> {
                                val v = arguments[0].eval()
                                val pair = obj.remove(Value(v.first, v.second))
                                return Pair(pair.getValue(), pair.getType())
                            }
                            "removeAll" -> {
                                val v = arguments[0].eval()
                                val pair = obj.removeAll(Value(v.first, v.second))
                                return Pair(pair.getValue(), pair.getType())
                            }
                            "length" -> {
                                val pair = obj.length()
                                return Pair(pair.getValue(), pair.getType())
                            }
                            "isEmpty" -> {
                                val pair = obj.isEmpty()
                                return Pair(pair.getValue(), pair.getType())
                            }
                            "contains" -> {
                                val v = arguments[0].eval()
                                val pair = obj.contains(Value(v.first, v.second))
                                return Pair(pair.getValue(), pair.getType())
                            }
                            else -> Runtime.raiseError("List does not have method '${methodName}'")
                        }
                    }
                    else -> Runtime.raiseError("Object ${obj.name()} does not exist")
                }
            }
        }
    }

    override fun toString(): String {
        return "[MethodCallExpr]"
    }
}