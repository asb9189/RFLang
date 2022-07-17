package nodes.statements

import nodes.interfaces.Statement
import nodes.root.Node

class FuncDef(name: String, params: List<String>, body: List<Statement>): Node(), Statement {

    private val name: String
    private val params: List<String>
    private val body: List<Statement>

    init {
        this.name = name
        this.params = params
        this.body = body
    }

    override fun toString(): String {
        return "[FuncDef]"
    }
}