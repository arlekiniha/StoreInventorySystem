package database.utils

import database.*
import java.io.File

class OpaValidator(
    private val file: File,
    private val columns: List<Column>,
) {

    /**
     * throws IllegalArgumentException if file extension or format is invalid
     * returns false when file is valid but empty
     * returns true when file extension and format are valid
     */
    private fun isMetadataValid(): Boolean {
        if (file.extension != "opa") error("Extension is not .opa")
        val lines = file.readText().nonEmptyLines()
        if (lines.isEmpty()) return false
        val title = lines.first()
        if (title.matches("\\[[A-Za-z]+]".toRegex()).not()) error("Title format is invalid")

//        val isColumnsValid = lines[1].matches("\\[(?:[A-Za-z:]+\\|)*[A-Za-z]+]".toRegex()) // todo change regex for columns
//        if (!isColumnsValid) error("Types format is invalid")

        val fileColumns = lines[1].inBrackets().split('|')
            .map {
                val (name, type, primaryKey) = it.split(':')
                require(primaryKey == "P" || primaryKey == "N")
                { "Column does not contain primary key property" }
                Column(name.lexeme, type.columnType, primaryKey.asPrimaryKey())
            }
        if(fileColumns.count { it.primaryKey } != 1) error("Table should contain exactly one primary key")
        if (fileColumns.size != columns.size) error("Existing types count are not matching with provided types count")
        val isTypesMatching = fileColumns.zip(this.columns).all { (f, t) -> f == t }
        if (isTypesMatching.not()) error("Existing types are not marching with provided types")
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

        val typedColumns = lines[1].inBrackets().split('|')
            .map {
                val (name, type, primaryKey) = it.split(':')
                require(primaryKey == "P" || primaryKey == "N")
                { "Column does not contain primary key property" }
                Column(name.lexeme, type.columnType, primaryKey.asPrimaryKey())
            }

        val rawRecords = lines
            .drop(2)
            .onEach { line ->
                require(line.isRecordSyntacticallyValid())
                { "Record syntax is invalid" }
            }.map { line -> line.split('|') }

        val typedRecords = rawRecords.map { raw ->
            raw.mapIndexed { index, r ->
                r.typed(typedColumns[index].type)
            }
        }

        typedRecords
            .forEach { record ->
                require(record.size == types.size)
                { "Record properties count is not matching with types size" }
                record.forEachIndexed { index, prop ->
                    prop matches typedColumns[index].type
                }
            }
        return true
    }
}
