
enum class TokenType{
    WORD, NUMBER, STRING, CHARACTER
}

open class Token(val typeName: String, val type: TokenType, val line: Int, val col: Int)

class WordToken(val value: String, line: Int, col: Int) : Token("word", TokenType.WORD, line, col)
class NumberLiteralToken(val value: Int, line: Int, col: Int) : Token("number-literal", TokenType.NUMBER, line, col)
class StringLiteralToken(val value: String, line: Int, col: Int) : Token("string-literal", TokenType.STRING, line, col)
class CharLiteralToken(val value: Char, line: Int, col: Int) : Token("character-literal", TokenType.CHARACTER, line, col)
