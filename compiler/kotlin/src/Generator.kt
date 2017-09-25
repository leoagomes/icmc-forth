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
                    "call ${sym.name}\n"
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
            val currentLevel = decisionContext.peek() + 1
            decisionContext.push(currentLevel)
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
            val currentLevel = decisionContext.pop()
            val levelString = "${functionName}_loop${currentLevel}"


        })
        dictionary.put("loop", { _ ->
            val currentLevel = decisionContext.pop()
            val levelString = "${functionName}_loop${currentLevel}"

            "${levelString}_ploop:\n" +
                    "call ft_rs_pop\n" +
                    "inc r0\n" +
                    "push r1\n" +
                    "mov r1, r0\n" +
                    "call ft_rs_pop\n" +
                    "cmp r1, r0\n" +
                    "call ft_rs_push\n" +
                    "mov r0, r1\n" +
                    "call ft_rs_pop" +
                    "pop r1\n" +
                    "jeg ${levelString}_leave\n" +
                    ""
        })
    }

    private fun abort(reason: String) {
        throw GeneratorAbortedException(reason)
    }

    private fun consumeToken() {
        currentToken = lexer.readToken()
    }

    fun consumeAbortOnEnd(expected: String = "a token") {
        consumeToken()
        if (currentToken.type == TokenType.END) {
            abort("Expected $expected, got ${currentToken.valueToString()}. ($currentToken)")
        }
    }


}
