import org.w3c.dom.Element
import java.io.*
import java.nio.*
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
    var i : Int = 0

    loopity@ do {
        when (i) {
            1 -> {
                i++
                continue@loopity
            }
            else ->
                    print(i)
        }
        i++
    } while(i < 10)
}
