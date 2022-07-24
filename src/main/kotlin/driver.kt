import evaluator.Evaluator
import parser.Parser
import runtime.Runtime
import java.io.File
import scanner.Scanner
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        displayUsage()
        exitProcess(0)
    }

    val filePath = args[0]
    if (!isValidFilePath(filePath)) {
        println("File '${filePath}' does not exist")
        exitProcess(0)
    }

    val tokens = Scanner(File(filePath).reader()).scan()
    println("tokens:")
    for (token in tokens) {
        println(token)
    }

    println("\n\n\n")

    val program = Parser(tokens).parse()
    println("Statements:")
    println(program.toString())

    Evaluator(program).run()

}

private fun isValidFilePath(filePath: String): Boolean {
    val file = File(filePath)
    return file.exists() && file.isFile
}

private fun displayUsage() {
    println("Usage: RFLang myfile.rf")
}