package standard_lib

import evaluator.*
import evaluator.Function
import runtime.Runtime
import standard_lib.objects.Object

class StandardLibBuilder {



    companion object {

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
                Value(-1, ValueType.NULL)
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

            standardLibList = standardLibList.plus(print)
            standardLibList = standardLibList.plus(input)
            return standardLibList
        }
    }
}