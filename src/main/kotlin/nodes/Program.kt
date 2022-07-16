package nodes

import nodes.interfaces.Statement
import nodes.root.Node

class Program (statements: List<Statement>) : Node() {

    private val statements: List<Statement>

    init {
        this.statements = statements
    }

    override fun toString(): String {
        var result = ""
        for (stmt in statements) {
            result += stmt.toString()
            result += "\n"
        }
        return result
    }
}