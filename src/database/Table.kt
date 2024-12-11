package database

import database.utils.OpaValidator
import database.utils.requireIndexOf
import database.utils.toTable
import database.utils.writeToFile
import java.io.File

internal class TableHelper(
    path: String,
    tableName: String,
    tableTypes: List<Lexeme>,
) {
    private val file = File(path)
    private val validator: OpaValidator = OpaValidator(file, tableTypes)
    private var table = TableRepresentation(tableName, tableTypes, emptyList())

    init {
        require(tableTypes.isNotEmpty()) { "Table must have at least one type" }
        if (validator.isRecordsValid().not()) transaction { table }
        else table = file.readText().toTable()
    }

    fun insert(record: WriteRecord) = transaction {
        require(table.types.size == record.properties.size)
        { "Provided record properties count are not the same as in table" }

        val records = table.records.toMutableList()
        records += ReadRecord(
            id = records.size,
            record.properties,
        )
        table.copy(records = records)
    }

    fun insertAll(records: List<WriteRecord>) = transaction {
        records.forEach {
            require(it.properties.size == table.types.size)
            { "Provided record properties count are not the same as in table" }
        }

        val tableRecords = table.records.toMutableList()
        records.forEach { record ->
            tableRecords += ReadRecord(
                id = tableRecords.size,
                record.properties,
            )
        }
        table.copy(records = tableRecords)
    }

    fun deleteFirstWhere(type: Lexeme, value: Lexeme) = transaction {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }

        val indexOfRecord = table.records
            .indexOfFirst { record: ReadRecord -> record.properties[indexOfType] == value }
        require(indexOfRecord != -1) { "no such value in a table" }

        table.copy(
            records = table.records.toMutableList().apply { removeAt(indexOfRecord) }
        )
    }

    fun deleteAllWhere(type: Lexeme, value: Lexeme) = transaction {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }

        val indexOfRecord = table.records
            .indexOfFirst { record: ReadRecord -> record.properties[indexOfType] == value }
        require(indexOfRecord != -1) { "no such value in a table" }

        table.copy(
            records = table
                .records
                .toMutableList()
                .apply {
                    removeAll { it.properties[indexOfType] == value }
                }
        )
    }

    fun deleteAll() = transaction { table.copy(records = emptyList()) }

    fun selectFirstWhere(type: Lexeme, value: Lexeme): ReadRecord? {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
        return table.records.firstOrNull { it.properties[indexOfType] == value }
    }

    fun selectAllWhere(type: Lexeme, value: Lexeme): List<ReadRecord> {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
        return table.records.filter { it.properties[indexOfType] == value }
    }

    fun selectAll(): List<ReadRecord> = table.records

    fun updateFirstWhere(type: Lexeme, value: Lexeme, record: WriteRecord) = transaction {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
        val indexOfRecord = table.records
            .indexOfFirst { it.properties[indexOfType] == value }
        require(indexOfRecord != -1) { "no such value in a table" }

        val records = table.records.toMutableList()
        records[indexOfRecord] = ReadRecord(
            id = indexOfRecord,
            record.properties,
        )
        table.copy(records = records)
    }

    private fun transaction(block: () -> TableRepresentation) {
        table = block()
        table.writeToFile(file)
    }
}
