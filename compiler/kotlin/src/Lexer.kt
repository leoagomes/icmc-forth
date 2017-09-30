import java.io.FileInputStream
import java.io.InputStreamReader

class LexerAbortedException(override var message: String) : Exception(message)

class Lexer(inputFile: String) {
    private var fileStream : FileInputStream = FileInputStream(inputFile)
    private var reader : InputStreamReader = InputStreamReader(fileStream)

    var currentCodePoint : Int = 0
    var line : Int = 1
    var col : Int = 0
    var codePointsRead : Int = 0

    val isTerminated : Boolean
        get() = currentCodePoint == -1

    init {
        consume()
    }

    private fun consume() {
        currentCodePoint = reader.read()

        codePointsRead++
        if (currentCodePoint == '\n'.toInt()) {
            line++
            col = 0
        } else
            col++
    }

    private fun abort(reason: String) {
        throw LexerAbortedException(reason)
    }

    private fun match(cp: Int) {
        if (currentCodePoint != cp)
            abort("Expected ${String(Character.toChars(cp))}, got ${String(Character.toChars(currentCodePoint))}.")
    }

    private fun match(char: Char) = match(char.toInt())

    private fun skipSpaces() {
        while (Character.isWhitespace(currentCodePoint) && !isTerminated)
            consume()
    }

    private fun advance(until: Int) {
        while (currentCodePoint != until && !isTerminated)
            consume()
        consume()
    }

    private fun advance(until: Char) = advance(until.toInt())

    private fun skipComments() {
        when (currentCodePoint.toChar()) {
            '\\' -> advance('\n')
            '(' -> advance(')')
            '[' -> advance('}')
        }
    }

    private fun skipAll() {
        do {
            skipSpaces()
            skipComments()
        } while (Character.isWhitespace(currentCodePoint) && !isTerminated)
    }

    private fun readNumberLiteral() : NumberLiteralToken {
        val cline = line
        val ccol = col

        if (!Character.isDigit(currentCodePoint))
            abort("Expected digit, got '${String(Character.toChars(currentCodePoint))}'.")

        val value = if (currentCodePoint.toChar() == '0') {
            consume()

            if (isTerminated || Character.isWhitespace(currentCodePoint))
                0
            else {
                match('x')
                readHexInteger()
            }
        } else {
            readInteger()
        }

        return NumberLiteralToken(value, cline, ccol)
    }

    private fun readString() : String {
        match('"')

        var str = ""

        consumeAbortOnEnd()
        while (currentCodePoint.toChar() != '"') {
            if (currentCodePoint.toChar() == '\\') {
                str += readEscapedCharValue()
                continue
            } else {
                str += String(Character.toChars(currentCodePoint))
            }
            consumeAbortOnEnd("character")
        }

        match('"')
        consume()

        return str
    }

    private fun readWordIdentifier() : String {
        var str = ""

        while (!Character.isWhitespace(currentCodePoint) && !isTerminated) {
            str += String(Character.toChars(currentCodePoint))
            consume()
        }

        return str
    }

    private fun readStringLiteral() : StringLiteralToken {
        val cline = line
        val ccol = col

        match('"')
        val value = readString()

        return StringLiteralToken(value, cline, ccol)
    }

    private fun readWord() : Token {
        val cline = line
        val ccol = col
        val value = readWordIdentifier()

        return when (value) {
            ":" -> ColonToken(cline, ccol)
            ";" -> SemiColonToken(cline, ccol)
            "'" -> QuoteToken(cline, ccol)
            "var" -> VarToken(cline, ccol)
            "entry" -> EntrypointToken(cline, ccol)
            "string" -> StringToken(cline, ccol)
            "array" -> ArrayToken(cline, ccol)
            "{" -> OpenCurlyToken(cline, ccol)
            "}" -> CloseCurlyToken(cline, ccol)
            else -> WordToken(value, cline, ccol)
        }
    }

    fun readToken() : Token {
        skipAll()

        return when {
            currentCodePoint == -1 -> return TheEndToken(line, col)
            Character.isDigit(currentCodePoint) -> readNumberLiteral()
            currentCodePoint.toChar() == '\'' -> readCharLiteral()
            currentCodePoint.toChar() == '"' -> readStringLiteral()
            else -> readWord()
        }
    }

    private fun consumeAbortOnEnd(expected: String = "something") {
        consume()
        if (currentCodePoint == -1)
            abort("Expected $expected, got end of file.")
    }

    private fun readInteger() : Int {
        var integerStr = ""
        if (!Character.isDigit(currentCodePoint))
            abort("Expected digit, got ${String(Character.toChars(currentCodePoint))}.")

        while (Character.isDigit(currentCodePoint)) {
            integerStr += String(Character.toChars(currentCodePoint))
            consumeAbortOnEnd("number")
        }

        return integerStr.toInt()
    }

    private fun readHexInteger() : Int {
        var integerStr = ""

        if (!Character.isDigit(currentCodePoint))
            abort("Expected digit, got ${String(Character.toChars(currentCodePoint))}.")

        while (Character.isDigit(currentCodePoint) || (Character.isAlphabetic(currentCodePoint) &&
                Character.toLowerCase(currentCodePoint).toChar() >= 'a' &&
                Character.toLowerCase(currentCodePoint).toChar() <= 'e')) {
            integerStr += String(Character.toChars(currentCodePoint))
            consumeAbortOnEnd()
        }

        return integerStr.toInt(16)
    }

    private fun readEscapedCharValue() : Char {
        match('\\')
        consumeAbortOnEnd()

        when {
            Character.isDigit(currentCodePoint) -> {
                val value = readInteger()
                return value.toChar()
            }
            Character.toLowerCase(currentCodePoint).toChar() == 'x' -> {
                consumeAbortOnEnd("digit")
                val value = readHexInteger()
                return value.toChar()
            }
            Character.toLowerCase(currentCodePoint).toChar() == 'n' -> {
                consume()
                return '\n'
            }
            currentCodePoint.toChar() == '"' -> {
                consume()
                return '\"'
            }
            currentCodePoint.toChar() == '\\' -> {
                consume()
                return '\\'
            }
            else -> {
                abort("Unrecognized escape '\\${String(Character.toChars(currentCodePoint))}'.")
            }
        }
        return '?'
    }

    private fun readCharLiteral() : CharLiteralToken {
        var value = '?'

        match('\'')

        val cline = line
        val ccol = col

        consumeAbortOnEnd()

        when {
            currentCodePoint.toChar() == '\\' -> value = readEscapedCharValue()
            currentCodePoint in 0 .. 127 -> {
                value = currentCodePoint.toChar()
                consumeAbortOnEnd("'")
            }
            else ->
                    abort("Unrecognized character literal '${String(Character.toChars(currentCodePoint))}'.")
        }

        match('\'')
        consume()

        return CharLiteralToken(value, cline, ccol)
    }
}