package standard_lib.objects

enum class ObjectType {
    USER_DEFINED,
    STANDARD_LIB
}

abstract class Object {
    abstract fun name(): String
    abstract fun type(): ObjectType
    // TODO method for getting all object methods with required params
    abstract override fun toString(): String
}