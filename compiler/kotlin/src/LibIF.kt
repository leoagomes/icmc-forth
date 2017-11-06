import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.*
import java.io.File

enum class SymbolType {
    VARIABLE, FUNCTION
}

class Symbol(val type: SymbolType, val name: String, val word: String, val snippet: String) {
    var variables : MutableList<Pair<String, Int>> = mutableListOf()
    var dependencies : MutableList<Pair<String, String>> = mutableListOf()
}

class Module(val name: String) {
    var symbols : MutableList<Symbol> = mutableListOf()
}

class LibIF(val directory: String) {
    var modules : MutableMap<String, Module> = mutableMapOf()

    fun loadModules() {
        val file = File(directory)
        recursiveLoadModules(file)
    }

    private fun recursiveLoadModules(file: File) {
        if (!file.isDirectory)
            return

        val insideFiles = file.listFiles()

        try {
            val moduleFile: File? = insideFiles.first { f -> f.name == "module.xml" }

            if (moduleFile != null)
                loadModule(moduleFile, file)
        } catch (e: NoSuchElementException) {
        }

        for (child in insideFiles)
            recursiveLoadModules(child)
    }

    private fun loadModule(moduleFile: File, parent: File) {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = dBuilder.parse(moduleFile)

        doc.documentElement.normalize()

        val moduleName = doc.documentElement.getElementsByTagName("name").item(0).textContent
        modules.putIfAbsent(moduleName, Module(moduleName))
        val module = modules[moduleName]

        val symbolNodes = doc.documentElement.getElementsByTagName("symbol")

        (0..(symbolNodes.length - 1))
                .map { symbolNodes.item(it) }
                .filter { it.nodeType == Node.ELEMENT_NODE }
                .map { it as Element }
                .forEach { addSymbolToModule(module, it, moduleFile) }
    }

    private fun addSymbolToModule(module: Module?, element: Element, parent: File) {
        if (module == null)
            return

        val type = element.getAttribute("type")
        val name = element.getElementsByTagName("name").item(0).textContent
        val word = element.getElementsByTagName("word").item(0).textContent

        val snippetElement = element.getElementsByTagName("snippet").item(0) as Element?

        val snippet = when {
            snippetElement == null -> ""
            snippetElement.hasAttribute("src") -> {
                val loadedSnippet = File(parent.parent, snippetElement.getAttribute("src"))
                loadedSnippet.readText()
            }
            else -> snippetElement.textContent
        }

        val symbol = Symbol(if (type == "function") { SymbolType.FUNCTION } else { SymbolType.VARIABLE }, name, word, snippet)

        val depNodes = element.getElementsByTagName("dep")
        (0..(depNodes.length - 1))
                .map { depNodes.item(it) }
                .filter { it.nodeType == Node.ELEMENT_NODE }
                .map { it as Element }
                .forEach {
                    symbol.dependencies.add(Pair(it.getAttribute("module"), it.getAttribute("name")))
                }

        val varNodes = element.getElementsByTagName("var")
        (0..(varNodes.length - 1))
                .map { varNodes.item(it) }
                .filter { it.nodeType == Node.ELEMENT_NODE }
                .map { it as Element }
                .filter { !(it.hasAttribute("declare") && it.getAttribute("declare") == "no") }
                .forEach {
                    symbol.variables.add(Pair(it.getAttribute("name"), Integer.parseInt(it.getAttribute("size"))))
                }

        module.symbols.add(symbol)
    }
}