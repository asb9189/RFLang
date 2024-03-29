package nodes.interfaces

enum class StatementType {
    VAR_DEC_STMT,
    VAR_ASSIGN_STMT,
    WHILE_STMT,
    REPEAT_STMT,
    FUNC_CALL_STMT,
    FUNC_DEF_STMT,
    METHOD_CALL_STMT,
    CONSTRUCTOR_CALL_STMT,
    RETURN_STMT,
    BREAK_STMT,
    END_STMT,
    IF_STMT,
    FOR_IN_STMT
}

interface Statement {
    fun getType(): StatementType
}