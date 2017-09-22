
enum class TokenType{
    WORD, NUMBER, STRING, CHARACTER
}

abstract class Token(val typeName: String, val type: TokenType, val line: Int, val col: Int) {
    abstract fun valueToString() : String
    override fun toString() : String = "Token(value: ${valueToString()} type: $typeName, line: $line, col: $col)"
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
