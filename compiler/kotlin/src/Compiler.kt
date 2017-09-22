
class Compiler(inputFile: String, outputFile: String){
    private var parser : Parser = Parser(inputFile)
    private var emitter : Emitter = Emitter(outputFile)

    fun compile() {

    }
}