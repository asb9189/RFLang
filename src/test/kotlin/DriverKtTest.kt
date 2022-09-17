import evaluator.Evaluator
import org.junit.BeforeClass
import org.junit.Test
import parser.Parser
import scanner.Scanner
import utils.Utils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.system.exitProcess
import kotlin.test.assertEquals


internal class DriverKtTest {

    companion object {

        var tests = mutableListOf<Triple<String, String, String>>()

        private val outContent = ByteArrayOutputStream()
        private val errContent = ByteArrayOutputStream()
        private val originalOut = System.out
        private val originalErr = System.err

        @BeforeClass @JvmStatic
        fun setup() {
            val files = getTestFiles()
            files.forEach { (testName, testPair) ->
                tests.add(Triple(testName, testPair.first, testPair.second))
            }
        }

        @JvmStatic
        fun getTestFiles(): HashMap<String, Pair<String, String>> {
            val path = System.getProperty("user.dir") + "/src/test/examples"
            val testDirectories = mutableListOf<Pair<String, String>>()
            File(path).walkTopDown().forEach {
                if (it.isDirectory && it.name != "examples" && it.name != "sandbox") {
                    testDirectories.add(Pair(it.name, it.path))
                }
            }

            val testFiles = hashMapOf<String, Pair<String, String>>()
            for (pair in testDirectories) {
                val files = mutableListOf<String>()
                File(pair.second).walkTopDown().forEach {
                    if (it.isDirectory) { return@forEach }
                    files.add(it.path)
                }

                if (files.size != 2) { exitProcess(-1) }
                testFiles[pair.first] = Pair(files[0], files[1])
            }

            return testFiles
        }

        @JvmStatic // Run as '@After'
        fun restoreStreams() {
            System.setOut(originalOut)
            System.setErr(originalErr)
            outContent.reset()
        }

        @JvmStatic // Run as '@Before'
        fun setUpStreams() {
            System.setOut(PrintStream(outContent))
            System.setErr(PrintStream(errContent))
        }
    }

    @Test
    fun runAllTests() {
        setUpStreams()
        for (test in tests) {
            val testName = test.first
            val testProgramFile = test.second
            val testProgramSolutionFile = test.third

            val file = Utils.getFile(testProgramFile)
            val tokens = Scanner(file.reader()).scan()
            val program = Parser(tokens).parse()
            Evaluator.run(program)

            val out = outContent.toString()
            val expected = File(testProgramSolutionFile).readText()

            assertEquals(expected, out, message = "Running test '$testName'")
            restoreStreams()
        }
    }
}