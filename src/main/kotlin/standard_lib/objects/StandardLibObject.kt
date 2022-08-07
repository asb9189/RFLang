package standard_lib.objects

abstract class StandardLibObject: Object() {

    override fun type(): ObjectType {
        return ObjectType.STANDARD_LIB
    }

    abstract override fun toString(): String
}