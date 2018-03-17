
enum class TokenType{
    WORD, NUMBER, STRING, CHARACTER, END, COLON, SEMICOLON, QUOTE, VAR, ENTRY, STRING_TOKEN, OPEN_CURLY, CLOSE_CURLY,
    ARRAY, IMPORT
}

abstract class Token(val typeName: String, val type: TokenType, val line: Int, val col: Int) {
    open fun valueToString() : String = typeName
    override fun toString() : String = "Token(value: ${valueToString()}, type: $typeName, line: $line, col: $col)"
}

class WordToken(val value: String, line: Int, col: Int) : Token("word", TokenType.WORD, line, col) {
    override fun valueToString(): String = value
}

class NumberLiteralToken(val value: Int, line: Int, col: Int) : Token("number-literal", TokenType.NUMBER, line, col) {
    override fun valueToString(): String = value.toString(10)
}

class StringLiteralToken(val value: String, line: Int, col: Int) : Token("string-literal", TokenType.STRING, line, col) {
    override fun valueToString(): String = "\"$value\""
}

class CharLiteralToken(val value: Char, line: Int, col: Int) : Token("character-literal", TokenType.CHARACTER, line, col) {
    override fun valueToString(): String = "$value"
}

class TheEndToken(line: Int, col: Int) : Token("the-end", TokenType.END, line, col) {
    override fun valueToString(): String = "<the end>"
    override fun toString(): String = "EndToken(line: $line, col: $col)"
}

class ColonToken(line: Int, col: Int) : Token("colon", TokenType.COLON, line, col) {
    override fun valueToString(): String = ":"
}

class SemiColonToken(line: Int, col: Int) : Token("semicolon", TokenType.SEMICOLON, line, col) {
    override fun valueToString(): String = ";"
}

class QuoteToken(line: Int, col: Int) : Token("quote", TokenType.QUOTE, line, col) {
    override fun valueToString(): String = "'"
}

class VarToken(line: Int, col: Int) : Token("var", TokenType.VAR, line, col)
class EntryPointToken(line: Int, col: Int) : Token("entry", TokenType.ENTRY, line, col)
class StringToken(line: Int, col: Int) : Token("string", TokenType.STRING_TOKEN, line, col)
class ArrayToken(line: Int, col: Int) : Token("array", TokenType.ARRAY, line, col)
class ImportToken(line: Int, col: Int) : Token("import", TokenType.IMPORT, line, col)

class OpenCurlyToken(line: Int, col: Int) : Token("{", TokenType.OPEN_CURLY, line, col)
class CloseCurlyToken(line: Int, col: Int) : Token("}", TokenType.CLOSE_CURLY, line, col)
