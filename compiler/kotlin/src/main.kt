import java.io.*

fun main(args: Array<String>) {
    var fileStream = FileInputStream("teste.txt")
    var reader = InputStreamReader(fileStream)

    var codepoint : Int = reader.read()

    while (codepoint != -1) {
        print(Character.toLowerCase(codepoint).toChar() in 'a'..'e')
    }
}