package evaluator

enum class ValueType {
    INTEGER,
    STRING,
    BOOLEAN,
    OBJECT,
    NULL
}

class Value(value: Any, type: ValueType) {

    private val value: Any
    private val type: ValueType

    init {
        this.value = value
        this.type = type
    }

    fun getValue(): Any {
        return value
    }

    fun getType(): ValueType {
        return type
    }

}