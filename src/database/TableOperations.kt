package database

import database.utils.requireIndexOf

fun TableHelper.insert(record: WriteRecord) = transaction { table ->
    require(table.types.size == record.properties.size)
    { "Provided record properties count are not the same as in table" }

    val records = table.records.toMutableList()
    records += ReadRecord(
        id = records.size,
        record.properties,
    )
    table.copy(records = records)
}

fun TableHelper.insertAll(records: List<WriteRecord>) = transaction { table ->
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

fun TableHelper.deleteFirstWhere(type: Lexeme, value: Lexeme) = transaction { table ->
    val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }

    val indexOfRecord = table.records
        .indexOfFirst { record: ReadRecord -> record.properties[indexOfType] == value }
    require(indexOfRecord != -1) { "no such value in a table" }

    table.copy(
        records = table.records.toMutableList().apply { removeAt(indexOfRecord) }
    )
}

fun TableHelper.deleteAllWhere(type: Lexeme, value: Lexeme) = transaction { table ->
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

fun TableHelper.deleteAll() = transaction { it.copy(records = emptyList()) }

fun TableHelper.selectFirstWhere(type: Lexeme, value: Lexeme): ReadRecord? = provide { table ->
    val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
    table.records.firstOrNull { it.properties[indexOfType] == value }
}

fun TableHelper.selectAllWhere(type: Lexeme, value: Lexeme): List<ReadRecord> = provide { table ->
    val indexOfType = table.types.requireIndexOf(type) { "No such type in a table" }
    table.records.filter { it.properties[indexOfType] == value }
}

fun TableHelper.selectAll(): List<ReadRecord> = provide { it.records }

fun TableHelper.updateFirstWhere(type: Lexeme, value: Lexeme, record: WriteRecord) = transaction { table ->
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
