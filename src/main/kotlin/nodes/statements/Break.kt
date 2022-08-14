package nodes.statements

import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class Break: Node(), Statement {

    override fun getType(): StatementType {
        return StatementType.BREAK_STMT
    }

    override fun toString(): String {
        return "[Break]"
    }
}