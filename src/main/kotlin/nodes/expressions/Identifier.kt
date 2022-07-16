package nodes.expressions

import nodes.interfaces.Expression
import nodes.root.Node

class Identifier (name: String): Node(), Expression {

    private val name: String
    init {
        this.name = name
    }

    override fun eval(): String {
        return name
    }

    override fun toString(): String {
        return this.name
    }
}