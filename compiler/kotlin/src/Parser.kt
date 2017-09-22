import java.io.*

class ParserAbortedException(override var message: String) : Exception(message)

class Parser(val lexer: Lexer) {
    var tokenList : MutableList<Token> = mutableListOf<Token>()

    fun buildTokenList() {
        while(!lexer.isTerminated) {
            tokenList.add(lexer.readToken())
        }
    }
}