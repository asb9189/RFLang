package runtime

import java.util.UUID

/**
 * Manages resources during execution of a RFLang program
 * such as variable UUID generation, raised exceptions,
 * checking for valid network connections, etc ...
 */
class Runtime {
    companion object {
        private val UUIDSet = HashSet<UUID>()

        fun generateUUID(): UUID {
            val uuid = UUID.randomUUID()

            if (UUIDSet.contains(uuid)) {
                raiseError("Failed to generate unique UUID for all variables")
            }

            UUIDSet.add(uuid).also {
                if (it.not()) {
                    raiseError("Internal error occured: UUIDSet.add(...) returned 'false'")
                }
            }
            return uuid
        }

        fun raiseError(msg: String): Nothing {
            throw ExceptionRF(msg)
        }
    }
}