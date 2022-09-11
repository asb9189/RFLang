package nodes.statements

import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.root.Node

class End: Node(), Statement {

    override fun getType(): StatementType {
        return StatementType.END_STMT
    }

    override fun toString(): String {
        return "[End]"
    }
}