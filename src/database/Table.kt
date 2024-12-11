package database

import java.io.File

interface Table {
    fun insert(record: WriteRecord)
    fun insertAll(records: List<WriteRecord>)

    fun deleteFirstWhere(type: Lexeme, value: Lexeme)
    fun deleteAllWhere(type: Lexeme, value: Lexeme)
    fun deleteAll()

    fun selectFirstWhere(type: Lexeme, value: Lexeme): ReadRecord?
    fun selectAllWhere(type: Lexeme, value: Lexeme): List<ReadRecord>
    fun selectAll(): List<ReadRecord>

    fun updateFirstWhere(type: Lexeme, value: Lexeme, record: WriteRecord)
}

fun Table(
    path: String,
    name: String,
    types: List<Lexeme>,
): Table = DefaultTable(
    path = path,
    tableName = name,
    tableTypes = types,
)

private class DefaultTable(
    path: String,
    tableName: String,
    tableTypes: List<Lexeme>,
) : Table {
    private val file = File(path)
    private val validator: OpaValidator = OpaValidator(file, tableTypes)
    private var table = TableRepresentation(tableName, tableTypes, emptyList())

    init {
        require(tableTypes.isNotEmpty()) { "Table must have at least one type" }
        if (validator.isRecordsValid().not()) transaction { table }
        else table = file.readText().toTable()
    }

    override fun insert(record: WriteRecord) = transaction {
        require(table.types.size == record.properties.size)
        { "Provided record properties count are not the same as in table" }

        val records = table.records.toMutableList()
        records += ReadRecord(
            id = records.size,
            record.properties,
        )
        table.copy(records = records)
    }

    override fun insertAll(records: List<WriteRecord>) = transaction {
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

    override fun deleteFirstWhere(type: Lexeme, value: Lexeme) = transaction {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }

        val indexOfRecord = table.records
            .indexOfFirst { record: ReadRecord -> record.properties[indexOfType] == value }
        require(indexOfRecord != -1) { "no such value in a table" }

        table.copy(
            records = table.records.toMutableList().apply { removeAt(indexOfRecord) }
        )
    }

    override fun deleteAllWhere(type: Lexeme, value: Lexeme) = transaction {
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

    override fun deleteAll() = transaction { table.copy(records = emptyList()) }

    override fun selectFirstWhere(type: Lexeme, value: Lexeme): ReadRecord? {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
        return table.records.firstOrNull { it.properties[indexOfType] == value }
    }

    override fun selectAllWhere(type: Lexeme, value: Lexeme): List<ReadRecord> {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
        return table.records.filter { it.properties[indexOfType] == value }
    }

    override fun selectAll(): List<ReadRecord> = table.records

    // todo check
    override fun updateFirstWhere(type: Lexeme, value: Lexeme, record: WriteRecord) = transaction {
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
