package database

import java.io.File

interface Table {
    fun insert(record: WriteRecord)
    fun insertAll(records: List<WriteRecord>)

    fun deleteFirstWhere(type: String, value: String)
    fun deleteAllWhere(type: String, value: String)
    fun deleteAll()

    fun selectFirstWhere(type: String, value: String): ReadRecord?
    fun selectAllWhere(type: String, value: String): List<ReadRecord>
    fun selectAll(): List<ReadRecord>

    fun updateFirstWhere(type: String, value: String, record: WriteRecord)
}

fun Table(
    path: String,
    name: String,
    types: List<String>,
    validateRecords: Boolean = false,
): Table = DefaultTable(
    path = path,
    tableName = name,
    tableTypes = types,
    validateRecords = validateRecords,
)

private class DefaultTable(
    path: String,
    tableName: String,
    tableTypes: List<String>,
    validateRecords: Boolean,
) : Table {
    private val file = File(path)
    private val validator: OpaValidator = OpaValidator(file)
    private var table = TableRepresentation(tableName, tableTypes, emptyList())

    init {
        require(tableTypes.isNotEmpty()) { "Table must have at least one type" }
        val isValid = if (validateRecords) validator.isRecordsValid() else validator.isMetadataValid()

        if (isValid.not()) transaction { table }
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

    override fun deleteFirstWhere(type: String, value: String) = transaction {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }

        val indexOfRecord = table.records
            .indexOfFirst { record: ReadRecord -> record.properties[indexOfType] == value }
        require(indexOfRecord != -1) { "no such value in a table" }

        table.copy(
            records = table.records.toMutableList().apply { removeAt(indexOfRecord) }
        )
    }

    override fun deleteAllWhere(type: String, value: String) = transaction {
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

    override fun selectFirstWhere(type: String, value: String): ReadRecord? {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
        return table.records.firstOrNull { it.properties[indexOfType] == value }
    }

    override fun selectAllWhere(type: String, value: String): List<ReadRecord> {
        val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
        return table.records.filter { it.properties[indexOfType] == value }
    }

    override fun selectAll(): List<ReadRecord> = table.records

    // todo check
    override fun updateFirstWhere(type: String, value: String, record: WriteRecord) = transaction {
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
