import java.net.URLDecoder
import java.nio.file.Paths
import kotlin.system.exitProcess

import kotlin.jvm.*

fun main(args: Array<String>) {
    //val compiler = Compiler(args[0], args[1], "")
    //val inputPath = "teste.txt"
    //val outputPath = "toto.txt"
    //val libifDir = "libif/modules"

    if (args.size < 2) {
        print("usage:\nifc <source> <binary-destination> [<libIF-directory>]\n")
        exitProcess(1)
    }

    val ifLocation = if (args.size >= 3)  {
        args[2]
    } else {
        Paths.get(Paths.get({}.javaClass.protectionDomain.codeSource.location.toURI()).parent.toAbsolutePath().toString(), "libif").toString()
    }

    val compiler = Compiler(args[0], args[1], ifLocation)
    //val compiler = Compiler(inputPath, outputPath, libifDir)

    try {
        compiler.compile()
    } catch (le: Exception) {
        print("${compiler.lexer.line}:${compiler.lexer.col}: ")
        print(le.message)
    }
}
