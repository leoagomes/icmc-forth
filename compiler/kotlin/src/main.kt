import org.w3c.dom.Element
import java.io.*
import java.nio.*
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
    var l = Lexer("teste.txt")
    var t : Token

    while (!l.isTerminated) {
        t = l.readToken()
        println(t)
    }


    //var root = File("libif/modules")
    //recList(root, "")

/*    var file = File("libif/modules/core/module.xml")
    var dbFactory = DocumentBuilderFactory.newInstance()
    var dBuilder = dbFactory.newDocumentBuilder()
    var doc = dBuilder.parse(file)
    doc.documentElement.normalize()
    println("Root element: ${doc.documentElement.nodeName}")

    var nameTag = doc.documentElement.getElementsByTagName("name").item(0).textContent
    println("Module name: $nameTag")

    var symbolList = doc.getElementsByTagName("symbol")
    var firstSymbol = symbolList.item(0) as Element
    println("Symbol type: ${firstSymbol.getAttribute("type")}")

    var vars = firstSymbol.getElementsByTagName("dep")

    println("number of deps: ${vars.length}")*/
}

fun recList(file: File, preindent: String) {
    if (file.isDirectory) {
        var list = file.listFiles()
        println("${preindent}${file.name}")
        list.forEach { f -> recList(f, preindent + "\t") }
    } else {
        println("${preindent}${file.name}")
    }
}