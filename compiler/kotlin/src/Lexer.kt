import java.io.FileInputStream
import java.io.InputStreamReader

class LexerAbortedException(override var message: String) : Exception(message)

class Lexer(inputFile: String) {
    private var fileStream : FileInputStream = FileInputStream(inputFile)
    private var reader : InputStreamReader = InputStreamReader(fileStream)

    var lastToken : String = ""
    var currentCodePoint : Int = 0
    var line : Int = 1
    var col : Int = 0
    var codePointsRead : Int = 0

    val isTerminated : Boolean
        get() = currentCodePoint == -1

    init {
        consume()
    }

    fun consume() {
        currentCodePoint = reader.read()

        codePointsRead++
        if (currentCodePoint == '\n'.toInt()) {
            line++
            col = 0
        } else
            col++
    }

    fun abort(reason: String) {
        throw LexerAbortedException(reason)
    }

    fun match(cp: Int) {
        if (currentCodePoint != cp)
            abort("Expected ${String(Character.toChars(cp))}, got ${String(Character.toChars(currentCodePoint))}.")
    }

    fun match(char: Char) = match(char.toInt())

    fun skipSpaces() {
        while (Character.isWhitespace(currentCodePoint) && !isTerminated)
            consume()
    }

    fun advance(until: Int) {
        while (currentCodePoint != until && !isTerminated)
            consume()
        consume()
    }

    fun advance(until: Char) = advance(until.toInt())

    fun skipComments() {
        when (currentCodePoint.toChar()) {
            '\\' -> advance('\n')
            '(' -> advance(')')
            '{' -> advance('}')
        }
    }

    fun skipAll() {
        do {
            skipSpaces()
            skipComments()
        } while (Character.isWhitespace(currentCodePoint) && !isTerminated)
    }

    fun readNumberLiteral() : NumberLiteralToken {
        var value : Int = 0
        var cline = line
        var ccol = col

        if (!Character.isDigit(currentCodePoint))
            abort("Expected digit, got '${String(Character.toChars(currentCodePoint))}'.")

        value = if (currentCodePoint.toChar() == '0') {
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

    fun readString() : String {
        match('"')

        var str : String = ""

        consumeAbortOnEnd()
        while (currentCodePoint.toChar() != '"') {
            str += if (currentCodePoint.toChar() == '\\') {
                readEscapedCharValue()
            } else {
                String(Character.toChars(currentCodePoint))
            }
            consumeAbortOnEnd("character")
        }

        match('"')
        consume()

        return str
    }

    fun readWordIdentifier() : String {
        var str = ""

        while (!Character.isWhitespace(currentCodePoint) && !isTerminated) {
            str += String(Character.toChars(currentCodePoint))
            consume()
        }

        return str
    }

    fun readStringLiteral() : StringLiteralToken {
        val cline = line
        val ccol = col

        match('"')
        val value = readString()

        return StringLiteralToken(value, cline, ccol)
    }

    fun readWord() : WordToken {
        val cline = line
        val ccol = col
        val value = readWordIdentifier()

        return WordToken(value, cline, ccol)
    }

    fun readToken() : Token {
        skipAll()

        return when {
            Character.isDigit(currentCodePoint) -> readNumberLiteral()
            currentCodePoint.toChar() == '\'' -> readCharLiteral()
            currentCodePoint.toChar() == '"' -> readStringLiteral()
            else -> readWord()
        }
    }

    fun consumeAbortOnEnd(what: String = "something") {
        consume()
        if (currentCodePoint == -1)
            abort("Expected $what, got end of file.")
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
                var value = readInteger()
                return value.toChar()
            }
            Character.toLowerCase(currentCodePoint).toChar() == 'x' -> {
                consumeAbortOnEnd("digit")
                var value = readHexInteger()
                return value.toChar()
            }
            Character.toLowerCase(currentCodePoint).toChar() == 'n' -> {
                return '\n'
            }
            currentCodePoint.toChar() == '"' -> {
                return '\"'
            }
            currentCodePoint.toChar() == '\\' -> {
                return '\\'
            }
            else -> {
                abort("Unrecognized escape '\\${String(Character.toChars(currentCodePoint))}'.")
            }
        }
        return '?'
    }

    fun readCharLiteral() : CharLiteralToken {
        var value : Char = '?'

        match('\'')

        var cline = line
        var ccol = col

        consumeAbortOnEnd()

        when {
            currentCodePoint.toChar() == '\\' -> value = readEscapedCharValue()
            currentCodePoint in 0 .. 127 -> value = currentCodePoint.toChar()
            else ->
                    abort("Unrecognized character literal '${String(Character.toChars(currentCodePoint))}'.")
        }

        consumeAbortOnEnd("'")
        match('\'')

        return CharLiteralToken(value, cline, ccol)
    }
}