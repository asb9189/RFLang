import evaluator.Evaluator
import parser.Parser
import runtime.ExceptionRF
import scanner.Scanner
import utils.Utils

const val ONE_ARGUMENT = 1

fun main(args: Array<String>) {

    when (args.size) {
        ONE_ARGUMENT -> {
            try {
                val file = Utils.getFile(args[0])
                val tokens = Scanner(file.reader()).scan()
                val program = Parser(tokens).parse()
                Evaluator.run(program)
            } catch (e: ExceptionRF) {
                e.message?.run {
                    println(e)
                }
            }
        }
        else -> Utils.displayUsage()
    }
}

