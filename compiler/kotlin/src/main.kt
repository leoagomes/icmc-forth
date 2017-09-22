import java.io.*

fun main(args: Array<String>) {
    var l : Lexer = Lexer("teste.txt")
    var t : Token

    while (!l.isTerminated) {
        t = l.readToken()
        println(t)
    }
}