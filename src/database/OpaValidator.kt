package database

import java.io.File

class OpaValidator(private val file: File) {

    /**
     * throws IllegalArgumentException if file extension or format is invalid
     * returns false when file is valid but empty
     * returns true when file extension and format are valid
     */
    fun isMetadataValid(): Boolean {
        if (file.extension != "opa") error("Extension is not .opa")
        val lines = file.readText().nonEmptyLines()
        if (lines.isEmpty()) return false
        val title = lines.first()
        if (title.matches("\\[[A-Za-z]+]".toRegex()).not()) error("Title format is invalid")

        val isTypesValid = lines[1].matches("\\[(?:[A-Za-z]+\\|)*[A-Za-z]+]".toRegex())
        if (!isTypesValid) error("Types format is invalid")
        return true
    }

    /**
     * returns false when metadata is invalid or records are empty
     * returns true when metadata is valid and records format is valid
     * throws exception when metadata or records format are invalid
     */
    fun isRecordsValid(): Boolean {
        if (isMetadataValid().not()) return false
        val lines = file.readText().nonEmptyLines()
        if (lines.size == 2) return false
        val types = lines[1].split('|')
        val records = lines.drop(2).map { line -> line.split('|') }

        records.forEach { record ->
            require(record.size == types.size + 1) { "Record properties count is not matching with types size" }
        }
        return true
    }
}
