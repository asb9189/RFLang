package utils

import java.io.File
import kotlin.system.exitProcess

sealed class Utils {
    companion object {
        fun displayUsage() {
            println("Usage: RFLang program.rf")
        }

        fun getFile(filePath: String): File {
            val file = isValidFilePath(filePath)
            if (file.second.not()) {
                println("File '${filePath}' does not exist")
                exitProcess(0)
            }
            return file.first
        }

        private fun isValidFilePath(filePath: String): Pair<File, Boolean> {
            val file = File(filePath)
            return Pair(file, file.exists() && file.isFile)
        }
    }
}