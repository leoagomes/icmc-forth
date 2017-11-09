
class Compiler(inputFile: String, outputFile: String, libIFDirectory: String){
    var libIF : LibIF = LibIF(libIFDirectory)
    var lexer : Lexer = Lexer(inputFile)
    var emitter : Emitter = Emitter(outputFile, libIF)
    var generator : Generator = Generator(lexer, libIF, emitter)

    init {
        libIF.loadModules()
    }

    fun compile() {
        generator.generate()
        emitter.solveDependencies()
        emitter.emitFinal()
    }
}