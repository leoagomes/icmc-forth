import org.w3c.dom.Element
import java.io.*
import java.nio.*
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
    //val compiler = Compiler(args[0], args[1], "")
    val inputPath = "teste.txt"
    val outputPath = "toto.txt"
    val libifDir = "libif/modules"

    val compiler = Compiler(inputPath, outputPath, libifDir)

    compiler.compile()
    //try {
    //    compiler.compile()
    //} catch (le: Exception) {
    //    print(le.message);
    //}
}
