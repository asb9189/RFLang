package standard_lib

import evaluator.*
import evaluator.Function
import runtime.Runtime

class StandardLibBuilder {



    companion object {

        fun buildStandardLib(): List<Function> {

            var standardLibList = emptyList<Function>()

            //print("...")
            val print = StandardLibFunction("print", listOf("message")) { params ->
                val (p1, p1Type) = params[0].eval()
                if (p1Type != ValueType.STRING) {
                    Runtime.raiseError("Function 'input' expects arguments: [message: String]")
                }
                println(p1 as String)
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