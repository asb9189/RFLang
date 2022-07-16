package tokens

class Token constructor(tokenType: TokenType, literal: String) {

    private val tokenType: TokenType
    private val literal: String

    init {
        this.tokenType = tokenType
        this.literal = literal
    }

    fun getType(): TokenType {
        return this.tokenType
    }

    override fun toString(): String {
        return "$tokenType: '$literal'"
    }
}