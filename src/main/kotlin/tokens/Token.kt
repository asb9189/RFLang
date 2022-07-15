package tokens

class Token constructor(tokenType: TokenType, literal: String) {

    private val tokenType: TokenType
    private val literal: String

    init {
        this.tokenType = tokenType
        this.literal = literal
    }

    override fun toString(): String {
        return "$tokenType: '$literal'"
    }
}