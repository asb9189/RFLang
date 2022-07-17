package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node

class Var(literal: StringLiteral) : Node(), Expression {

    private val literal: StringLiteral

    init {
        this.literal = literal
    }

    override fun eval() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return this.literal.toString()
    }
}