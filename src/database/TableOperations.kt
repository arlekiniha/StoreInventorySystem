package database

import database.utils.findIndexOf
import database.utils.matches

fun TableHelper.insert(record: WriteRecord) = transaction { table ->
    record.properties matches table.columns

    val records = table.records.toMutableList()
    records += ReadRecord(
        id = records.size,
        record.properties,
    )
    table.copy(records = records)
}

fun TableHelper.insertAll(records: List<WriteRecord>) = transaction { table ->
    records.forEach {
        it.properties matches table.columns
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

// todo check
fun TableHelper.deleteFirstWhere(column: Column, value: Property) = transaction { table ->
    val indexOfType = table.columns.findIndexOf(column) { "No such type in a table" }

    val indexOfRecord = table.records
        .indexOfFirst { record: ReadRecord -> record.properties[indexOfType] == value }
    require(indexOfRecord != -1) { "no such value in a table" }

    table.copy(
        records = table.records.toMutableList().apply { removeAt(indexOfRecord) }
    )
}

// todo check
fun TableHelper.deleteAllWhere(column: Column, value: Property) = transaction { table ->
    val indexOfType = table.columns.findIndexOf(column) { "No such type in a table" }

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

fun TableHelper.selectFirstWhere(column: Column, value: Property): ReadRecord? = provide { table ->
    val indexOfType = table.columns.findIndexOf(column) { "No such type in a table" }
    table.records.firstOrNull { it.properties[indexOfType] == value }
}

fun TableHelper.selectAllWhere(column: Column, value: Property): List<ReadRecord> = provide { table ->
    val indexOfType = table.columns.findIndexOf(column) { "No such type in a table" }
    table.records.filter { it.properties[indexOfType] == value }
}

fun TableHelper.selectAll(): List<ReadRecord> = provide { it.records }

fun TableHelper.updateFirstWhere(column: Column, value: Property, record: WriteRecord) = transaction { table ->
    record.properties matches table.columns
    val indexOfType = table.columns.findIndexOf(column) { "No such type in a table" }
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
