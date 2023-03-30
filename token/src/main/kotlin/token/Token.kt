package token

data class Token(val type: TokenType, val location: Location, val originalValue: String, val length: Int) {
    fun actualValue() =
        if (type == TokenType.STRING_LITERAL) originalValue.trim(originalValue[0]) else originalValue
}

data class Location(val row: Int, val column: Int)

enum class TokenType {
    NUMBER_KEYWORD,
    STRING_KEYWORD,
    NUMBER_LITERAL,
    STRING_LITERAL,
    IDENTIFIER,
    LET_KEYWORD,
    OPERATOR_PLUS,
    OPERATOR_MINUS,
    OPERATOR_TIMES,
    OPERATOR_DIVIDE,
    DOUBLE_DOTS,
    SEMI_COLON,
    ASIGNATION_EQUALS,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    WHITE_SPACE,
    COMMENT,
    ERROR,
    UNKNOWN
}
