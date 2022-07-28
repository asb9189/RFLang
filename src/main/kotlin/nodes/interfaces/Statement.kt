package nodes.interfaces

enum class StatementType {
    VAR_DEC_STMT,
    VAR_ASSIGN_STMT,
    WHILE_STMT,
    REPEAT_STMT,
    FUNC_CALL_STMT,
    FUNC_DEF_STMT,
    RETURN_STMT,
    IF_STMT
}

interface Statement {
    abstract fun getType(): StatementType
}