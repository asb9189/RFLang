import evaluator.Evaluator
import org.junit.After
import org.junit.Before
import org.junit.Test
import parser.Parser
import scanner.Scanner
import utils.Utils
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals


internal class DriverKtTest {

    private val empty = "src/test/examples/empty.rf"

    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

    @Before
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }

    @After
    fun restoreStreams() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    @Test
    fun out() {
        print("hello")
        assertEquals("hello", outContent.toString())
    }

    @Test
    fun err() {
        System.err.print("hello again")
        assertEquals("hello again", errContent.toString())
    }

    @Test
    fun testEmpty() {
        val file = Utils.getFile(empty)
        val tokens = Scanner(file.reader()).scan()
        val program = Parser(tokens).parse()
        Evaluator.run(program)

        assertEquals(String(), outContent.toString())
    }
}