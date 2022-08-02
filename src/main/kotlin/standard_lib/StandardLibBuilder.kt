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
                val (p1, p1Type) = params[0].eval()

                when (p1Type) {
                    ValueType.INTEGER -> println(p1 as Int)
                    ValueType.BOOLEAN -> println(p1 as Boolean)
                    ValueType.STRING -> println(p1 as String)
                    ValueType.OBJECT -> println(p1 as Object)
                    ValueType.NULL -> Runtime.raiseError("Function 'print' cannot print type NULL")
                }
                Value(-1, ValueType.NULL)
            }

            // input("...")
            val input = StandardLibFunction("input", listOf("message")) { params ->
                val (p1, p1Type) = params[0].eval()
                if (p1Type != ValueType.STRING) {
                    Runtime.raiseError("Function 'input' expects arguments: [message: String]")
                }
                
                print(p1 as String)
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