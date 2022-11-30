package utils

import java.io.File
import runtime.Runtime

sealed class Utils {
    companion object {
        fun displayUsage() {
            println("Usage: RFLang program.rf")
        }

        fun getFile(filePath: String): File {
            val file = isValidFilePath(filePath)
            if (file.second.not()) {
                Runtime.raiseError("File '${filePath}' does not exist")
            }
            return file.first
        }

        private fun isValidFilePath(filePath: String): Pair<File, Boolean> {
            val file = File(filePath)
            return Pair(file, file.exists() && file.isFile)
        }
    }
}