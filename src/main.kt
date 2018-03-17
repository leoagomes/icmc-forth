import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

import kotlin.jvm.*

fun main(args: Array<String>) {
    if (args.size < 2) {
        print("usage:\nifc <source> <binary-destination> [<libIF-directory>]\n")
        exitProcess(1)
    }

    val ifLocation = if (args.size >= 3)  {
        args[2]
    } else {
        Paths.get(Paths.get({}.javaClass.protectionDomain.codeSource.location.toURI()).parent.toAbsolutePath().toString(), "libif").toString()
    }

    val includeDirectories: List<String> = if (args.size > 3) {
        args.asList().subList(3, args.size)
    } else {
        Collections.emptyList()
    }

    val compiler = Compiler(args[0], args[1], ifLocation, includeDirectories)

    try {
        compiler.compile()
    } catch (le: Exception) {
        println(le.message)
    }
}
