import java.io.*

class ParserAbortedException(override var message: String) : Exception(message)

class Parser(val lexer: Lexer) {
    var tokenList : MutableList<Token> = MutableList()

    fun buildTokenList() {
        while(!lexer.isTerminated) {
            tokenList.add(lexer.readToken())
        }
    }
}