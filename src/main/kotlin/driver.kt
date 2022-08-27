import evaluator.Evaluator
import parser.Parser
import scanner.Scanner
import utils.Utils

const val ONE_ARGUMENT = 1

fun main(args: Array<String>) {

    when (args.size) {
        ONE_ARGUMENT -> {
            val file = Utils.getFile(args[0])
            val tokens = Scanner(file.reader()).scan()
            val program = Parser(tokens).parse()
            Evaluator.run(program)
        }
        else -> Utils.displayUsage()
    }
}

