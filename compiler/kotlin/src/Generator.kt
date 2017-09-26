import java.util.*

class GeneratorAbortedException(override var message: String) : Exception(message)

class Generator(val lexer: Lexer, val libIF: LibIF, val emitter: Emitter) {
    var entryPoint : String = "main"
    var currentToken : Token = TheEndToken(0,0)
    var dictionary : MutableMap<String, (w: String) -> String> = mutableMapOf()

    private var decisionContext : Stack<Int> = Stack()
    private var decisionElseContext : Stack<Boolean> = Stack()
    private var loopContext : Stack<Int> = Stack()
    private var functionName : String = "ft_nonfunc_stub"

    fun bootstrapDictionary() {
        // adding core functions
        libIF.modules.forEach { name, module ->
            module.symbols.forEach { sym ->
                dictionary.put(sym.word, { word ->
                    emitter.addRootDependency(module.name, sym.name)

                    when (sym.type) {
                        SymbolType.FUNCTION -> "call ${sym.name}\n"
                        SymbolType.VARIABLE -> {
                            emitter.addRootDependency("core", "ft_ds_push")

                            "loadn r0, #${sym.name}\n" +
                                    "call ft_ds_push\n"
                        }
                    }
                })
            }
        }

        // add keywords
        dictionary.put("if", { _ ->
            val currentLevel = decisionContext.peek() + 1
            decisionContext.push(currentLevel)
            decisionElseContext.push(0)

            val levelString = "${functionName}_if${currentLevel}"
            decisionContext.push(currentLevel)

            emitter.addRootDependency("core", "ft_ds_pop")

            "${levelString}_prepare:\n" +
                    "call ft_ds_pop\n" +
                    "push r1\n" +
                    "xor r1, r1, r1\n" +
                    "cmp r0, r1\n" +
                    "pop r1\n" +
                    "jz ${levelString}_after\n" +
                    "${levelString}_in:\n"
        })
        dictionary.put("else", { _ ->
            val currentLevel = decisionContext.peek()
            if (currentLevel == -1)
                abort("More elses than ifs.")
            if (decisionElseContext.peek())
                abort("Multiple elses for an if.")

            val levelString = "${functionName}_if${currentLevel}"

            decisionElseContext.pop()
            decisionElseContext.push(true)

            "jmp ${levelString}_after_else\n" +
                    "${levelString}_after:\n"
        })
        dictionary.put("then", { _ ->
            val currentLevel = decisionContext.pop()
            if (currentLevel == -1)
                abort("More thens than ifs.")

            val levelString = "${functionName}_if${currentLevel}"

            if (decisionElseContext.pop()) {
                "${levelString}_after_else:\n"
            } else {
                "${levelString}_after:\n"
            }
        })

        dictionary.put("do", { _ ->
            val currentLevel = loopContext.peek() + 1
            loopContext.push(currentLevel)
            val levelString = "${functionName}_loop${currentLevel}"

            emitter.addRootDependency("core", "ft_ds_swap")
            emitter.addRootDependency("core", "ft_rs_ds2rs")

             "${levelString}_prepare:\n" +
                     "call ft_ds_swap\n" +
                     "call ft_rs_ds2rs\n" +
                     "call ft_rs_ds2rs\n" +
                     "${levelString}_begin:\n"
        })
        dictionary.put("leave", { _ ->
            val currentLevel = loopContext.pop()

            if (currentLevel == -1)
                abort("No loop to leave.")

            val levelString = "${functionName}_loop${currentLevel}"

            "jmp ${levelString}_leave\n"
        })
        dictionary.put("loop", { _ ->
            val currentLevel = loopContext.pop()

            if (currentLevel == -1)
                abort("Not 'do'ing anything. (Can't loop from nothing)")

            val levelString = "${functionName}_loop${currentLevel}"

            emitter.addRootDependency("core", "ft_rs_pop")
            emitter.addRootDependency("core", "ft_rs_push")

            "${levelString}_ploop:\n" +
                    "call ft_rs_pop\n" +
                    "inc r0\n" +
                    "push r1\n" +
                    "mov r1, r6\n" +
                    "dec r1\n" +
                    "loadi r1, r1\n" +
                    "cmp r0, r1\n" +
                    "push fr\n" +
                    "call ft_rs_push\n" +
                    "pop fr\n" +
                    "pop r1\n" +
                    "jle ${levelString}_begin\n" +
                    "${levelString}_leave:\n" +
                    "dec r6\n" +
                    "dec r6\n"
        })
        dictionary.put("+loop", { _ ->
            val currentLevel = loopContext.pop()

            if (currentLevel == -1)
                abort("Not 'do'ing anything. (Can't +loop from nothing)")

            val levelString = "${functionName}_loop${currentLevel}"

            emitter.addRootDependency("core", "ft_ds_pop")
            emitter.addRootDependency("core", "ft_rs_pop")
            emitter.addRootDependency("core", "ft_rs_push")

            "${levelString}_ploop:\n" +
                    "call ft_rs_pop\n" +
                    "push r1\n" +
                    "mov r1, r0\n" +
                    "call ft_ds_pop\n" +
                    "add r0, r0, r1\n" +
                    "mov r1, r6\n" +
                    "dec r1\n" +
                    "loadi r1, r1\n" +
                    "cmp r0, r1\n" +
                    "push fr\n" +
                    "call ft_rs_push\n" +
                    "pop fr\n" +
                    "pop r1\n" +
                    "jle ${levelString}_begin\n" +
                    "${levelString}_leave:\n" +
                    "call ft_rs_pop\n" +
                    "call ft_rs_pop\n"
        })

        dictionary.put("begin", { _ ->
            val currentLevel = loopContext.peek() + 1
            loopContext.push(currentLevel)
            val levelString = "${functionName}_loop${currentLevel}"

            "${levelString}_begin:\n"
        })
        dictionary.put("until", { _ ->
            val currentLevel = loopContext.pop()

            if (currentLevel == -1)
                abort("No 'begin' matching your 'until'.")

            val levelString = "${functionName}_loop${currentLevel}"

            emitter.addRootDependency("core", "ft_ds_pop")

            "${levelString}_puntil:\n" +
                    "call ft_ds_pop\n" +
                    "push r1\n" +
                    "xor r1, r1, r1\n" +
                    "cmp r0, r1\n" +
                    "pop r1\n" +
                    "jeq ${levelString}_begin\n"
        })
        dictionary.put("while", { _ ->
            val currentLevel = loopContext.peek()

            if (currentLevel == -1)
                abort("No 'begin' matching your 'while'.")

            val levelString = "${functionName}_loop${currentLevel}"

            emitter.addRootDependency("core", "ft_ds_pop")

            "${levelString}_pwhile:\n" +
                    "call ft_ds_pop\n" +
                    "push r1\n" +
                    "xor r1, r1, r1\n" +
                    "cmp r0, r1\n" +
                    "pop r1\n" +
                    "jeq ${levelString}_leave\n"
        })
        dictionary.put("repeat", { _ ->
            val currentLevel = loopContext.pop()

            if (currentLevel == -1)
                abort("No 'begin' matching your 'repeat'.")

            val levelString = "${functionName}_loop${currentLevel}"

            "${levelString}_prepeat:\n" +
                    "jmp ${levelString}_begin\n" +
                    "${levelString}_leave:\n"
        })

        // TODO: add iterators


    }

    private fun handleEntryToken() : String {
        val nextToken = consumeAbortOnEnd("a word")

        if (nextToken.type != TokenType.WORD)
            abort("'entry' expects a word as argument. Got ${nextToken.typeName}.")

        entryPoint = nextToken.valueToString()

        return ""
    }

    private fun matchTokenType(type: TokenType, expected: String, msg: String? = null) {
        val message = msg ?: "Expected $expected, but got ${currentToken.typeName}"

        if (currentToken.type != type)
            abort(message)
    }

    private fun handleColonToken() : String {
        val fnNameToken = consumeAbortOnEnd("function name")

        matchTokenType(TokenType.WORD, "function name")

        var fnName = (fnNameToken as WordToken).value

        do {
            when (ct.type) {
                // TODO: FINISH
            }
        } while (currentToken.type != TokenType.SEMICOLON)
    }

    private fun mangleVariableName(name: String) : String {
        return name.map {
            if ("$it".matches(Regex("[a-zA-Z0-9_]")))
                "$it"
            else
                "_${it.toInt()}"
        }.reduce { before, after ->
            before + after
        }
    }

    private fun handleVarToken() : String {
        val variableName : String
        val variableValue : Int

        val nameToken = consumeAbortOnEnd("a word")

        if (nameToken.type != TokenType.WORD)
            abort("'var' expects a word as argument, got ${nameToken.typeName}.")

        variableName = nameToken.valueToString()

        val valueToken = consumeToken()

        variableValue = when (valueToken.type) {
            TokenType.NUMBER -> (valueToken as NumberLiteralToken).value
            TokenType.CHARACTER -> (valueToken as CharLiteralToken).value.toInt()
            else -> 0
        }

        val mangledName = mangleVariableName(variableName)

        if (emitter.stringList.containsKey(mangledName))
            abort("There is already a string with the name '$variableName'.")
        if (emitter.variableList.containsKey(mangledName))
            abort("Variable redefinition is not allowed. (variable: $variableName)")
        if (emitter.functionList.containsKey(mangledName))
            abort("There is already a function with the name '$variableName'.")

        emitter.addVariable(mangledName, variableValue)

        dictionary[variableName] = { name ->
            val mangled = mangleVariableName(name)
            emitter.addRootDependency("core", "ft_ds_push")
            "loadn r0, #$mangled\n" +
                    "call ft_ds_push\n"
        }

        return ""
    }

    private fun handleStringToken() : String {
        val variableName : String
        val variableValue : String

        val nameToken = consumeAbortOnEnd("a word")

        if (nameToken.type != TokenType.WORD)
            abort("'string' expects a word as argument, got ${nameToken.typeName}.")

        variableName = nameToken.valueToString()

        val stringToken = consumeAbortOnEnd("a word")

        if (stringToken.type != TokenType.STRING)
            abort("'string' expects a string literal after the word. Got ${stringToken.typeName}")

        variableValue = (stringToken as StringLiteralToken).value

        val mangledName = mangleVariableName(variableName)

        if (emitter.stringList.containsKey(mangledName))
            abort("String redefinition is not allowed. (string: $variableName)")
        if (emitter.variableList.containsKey(mangledName))
            abort("There is already a variable with the name '$variableName'.")
        if (emitter.functionList.containsKey(mangledName))
            abort("There is already a function with the name '$variableName'.")

        emitter.addStringLiteral(content = variableValue, name = mangledName)

        dictionary[variableName] = { name ->
            val mangled = mangleVariableName(name)
            emitter.addRootDependency("core", "ft_ds_push")
            "loadn r0, #$mangled\n" +
                    "call ft_ds_push\n"
        }

        return ""
    }

    private fun handleArrayToken() : String {
        
    }

    private fun abort(reason: String) {
        throw GeneratorAbortedException(reason)
    }

    private fun consumeToken() : Token {
        currentToken = lexer.readToken()
        return currentToken
    }

    fun consumeAbortOnEnd(expected: String = "a token") : Token {
        consumeToken()
        if (currentToken.type == TokenType.END) {
            abort("Expected $expected, got ${currentToken.valueToString()}. ($currentToken)")
        }
        return currentToken
    }


}

