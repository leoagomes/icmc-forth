
class Compiler(inputFile: String, outputFile: String){
    private var lexer : Lexer = Lexer(inputFile)
    private var parser : Parser = Parser(lexer)
    private var emitter : Emitter = Emitter(outputFile)

    fun compile() {

    }
}