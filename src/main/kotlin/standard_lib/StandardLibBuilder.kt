package standard_lib

import evaluator.*
import evaluator.Function
import runtime.Runtime
import standard_lib.objects.Object
import standard_lib.objects.ObjectType
import kotlin.system.exitProcess

class StandardLibBuilder {

    companion object {

        private val stdlibObjects = listOf(
            "List"
        )

        fun buildStandardLibObjects(): HashMap<String, ObjectType> {
            val map = HashMap<String, ObjectType>()
            stdlibObjects.forEach { item ->
                map[item] = ObjectType.STANDARD_LIB
            }

            return map
        }

        fun buildStandardLibFunctions(): List<Function> {

            var standardLibList = emptyList<Function>()

            //print("...")
            val print = StandardLibFunction("print", listOf("message")) { params ->
                val value = params[0].eval()

                when (value.getType()) {
                    ValueType.INTEGER -> println(value.getValue() as Int)
                    ValueType.BOOLEAN -> println(value.getValue() as Boolean)
                    ValueType.STRING -> println(value.getValue() as String)
                    ValueType.OBJECT -> println(value.getValue() as Object)
                    ValueType.NULL -> Runtime.raiseError("Function 'print' cannot print type NULL")
                }
                Value(Value.Companion.NULL(), ValueType.NULL)
            }

            // input("...")
            val input = StandardLibFunction("input", listOf("message")) { params ->
                val value = params[0].eval()
                if (value.getType() != ValueType.STRING) {
                    Runtime.raiseError("Function 'input' expects arguments: [message: String]")
                }
                
                print(value.getValue() as String)
                val userInput = readLine()
                userInput?.let {
                    return@StandardLibFunction Value(it, ValueType.STRING)
                } ?: run {
                    Runtime.raiseError("Failed to read line from standard in during input(...)")
                }
            }

            val exit = StandardLibFunction("exit", listOf()) {
                exitProcess(0)
            }

            // abs(...)
            val abs = StandardLibFunction("abs", listOf("message")) { params ->
                val value = params[0].eval()
                if (value.getType() != ValueType.INTEGER) {
                    Runtime.raiseError("Function 'input' expects arguments: [message: String]")
                }

                val num = value.getValue() as Int
                if (num < 0) {
                    return@StandardLibFunction Value((num * -1), ValueType.INTEGER)
                }
                return@StandardLibFunction Value(num, ValueType.INTEGER)
            }

            standardLibList = standardLibList.plus(print)
            standardLibList = standardLibList.plus(input)
            standardLibList = standardLibList.plus(exit)
            standardLibList = standardLibList.plus(abs)
            return standardLibList
        }
    }
}