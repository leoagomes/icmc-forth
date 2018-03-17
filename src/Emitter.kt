import java.io.*

class Emitter(outFilePath: String, private val libIF: LibIF){
    private var outputFile : FileOutputStream = FileOutputStream(outFilePath)

    var variableList : MutableMap<String, Int> = mutableMapOf()
    var stringList : MutableMap<String, String> = mutableMapOf()
    var functionList : MutableMap<String, String> = mutableMapOf()
    var staticsList : MutableMap<String, MutableList<Int>> = mutableMapOf()
    var rootDependencies : MutableList<Pair<String, String>> = mutableListOf()
    var firstChunk : String = "; created by the if compiler\n"


    private fun escapeString(text: String) : String {
        text.replace("\n", "\\n")
        text.replace("\"", "\\\"")
        text.replace("\\", "\\\\")

        return text
    }

    private fun hashString(str: String) : Long {
        var hash : Long = 5381

        for(c in str)
            hash = (hash.shl(5) + hash) + c.toInt()

        return Math.abs(hash)
    }

    fun addVariable(name: String, size: Int) = variableList.put(name, size)
    fun addFunction(name: String, contents: String) = functionList.put(name, contents)
    fun addStringLiteral(content: String, name: String? = null) : String {
        val escaped = escapeString(content)
        val hash = hashString(content)

        val strName = name ?: "STRL_$hash"

        if (stringList.containsKey(strName))
            return strName

        return if (!stringList.containsValue(escaped)) {
            stringList[strName] = escaped
            strName
        } else {
            stringList.filter { entry -> entry.value == content }.keys.first()
        }
    }

    private fun hasStringLiteral(literal: String) : Boolean {
        val hashed = hashString(literal)
        val strName = "STRL_$hashed"

        if (stringList.containsKey(strName))
            return true

        return stringList.containsValue(escapeString(literal))
    }

    fun getNameForStringLiteral(literal: String) : String? {
        if (!hasStringLiteral(literal))
            return null

        val escaped = escapeString(literal)

        return stringList.filter { it.value == escaped }.keys.first()
    }

    fun addRootDependency(module: String, name: String) {
        val matching = rootDependencies.filter { p -> p.first == module && p.second == name }

        if (matching.isEmpty())
            rootDependencies.add(Pair(module, name))
    }

    fun solveDependencies() {
        for (dependency in rootDependencies) {
            recSolveDependencies(dependency)
        }
    }

    private fun recSolveDependencies(dependency: Pair<String, String>) {
        val depName = dependency.second
        val depModule = dependency.first

        if (functionList.contains(depName) || variableList.containsKey(depName))
            return

        if (!libIF.modules.contains(depModule))
            return

        val module = libIF.modules[depModule]
        val symbol = module!!.symbols.first { s -> s.name == depName }

        symbol.dependencies.forEach { s -> recSolveDependencies(s) }

        symbol.variables.forEach { v -> addVariable(v.first, v.second) }

        when (symbol.type) {
            SymbolType.VARIABLE -> symbol.variables.forEach { v -> addVariable(v.first, v.second) }
            SymbolType.FUNCTION -> addFunction(depName, symbol.snippet)
        }
    }

    private fun emitChunk(chunk: String) {
        outputFile.write(chunk.toByteArray())
    }

    fun emitFinal() {
        emitChunk(firstChunk)

        emitChunk("\n\n; *** VARIABLES ***\n")
        variableList.forEach { name, size ->
            emitChunk("$name: var #$size\n")
        }
        emitChunk("\n; *** END OF VARIABLES ***\n")

        emitChunk("\n\n; *** STRINGS ***\n")
        stringList.forEach { name, contents ->
            emitChunk("$name: string \"$contents\"\n")
        }
        emitChunk("\n\n; *** END OF STRINGS ***\n")

        emitChunk("\n\n; *** FUNCTIONS ***\n")
        functionList.forEach { name, contents ->
            emitChunk("; &&& $name\n")
            emitChunk(contents)
            emitChunk("\n; &&& end of $name\n\n")
        }
        emitChunk("\n\n; *** END OF FUNCTIONS ***\n")

        emitChunk("\n\n; *** STATIC ***\n")
        staticsList.forEach { name, values ->
            var chunk = ""

            values.forEachIndexed { index, value ->
                chunk += "static $name + #$index, #$value\n"
            }
        }
        emitChunk("\n; *** END OF STATIC ***\n")

        // Puts the HERE label last. Keep it here as code may depend on this.
        emitChunk("FT_HERE_LABEL:")
    }
}