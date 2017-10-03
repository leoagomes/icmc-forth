
class Compiler(inputFile: String, outputFile: String, libIFDirectory: String){
    private var libIF : LibIF = LibIF(libIFDirectory)
    private var lexer : Lexer = Lexer(inputFile)
    private var emitter : Emitter = Emitter(outputFile, libIF)
    private var generator : Generator = Generator(lexer, libIF, emitter)

    init {
        libIF.loadModules()
    }

    fun compile() {
        generator.generate()
        emitter.emitFinal()
    }
}