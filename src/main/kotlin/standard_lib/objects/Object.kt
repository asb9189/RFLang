package standard_lib.objects

enum class TYPE {
    LIST
}

abstract class Object {
    abstract fun type(): TYPE
    abstract override fun toString(): String
}