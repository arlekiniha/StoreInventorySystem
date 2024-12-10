package database

import java.io.File

class Dao(
    path: String,
    tableName: String,
    tableTypes: List<String>,
) {
    private val file = File(path)
    var table: Table
        private set

    init {
        if (file.readText().isBlank()) {
            Table(tableName, tableTypes, emptyList()).writeToFile(file)
        }
        val lines = file.readLines()
        val title = lines[0].inBrackets()
        val types = lines[1].inBrackets().split('|')
        val properties = lines.drop(2).map { record ->
            val props = record.split('|')
            ReadRecord(
                id = props.first().toInt(),
                properties = props.drop(1),
            )
        }
        table = Table(
            name = title,
            types = types,
            records = properties,
        )
    }

    fun insert(record: WriteRecord) {
        require(table.types.size == record.properties.size) { "Idi nahui mnogo ili malo properdey" }

        val records = table.records.toMutableList()
        records += ReadRecord(
            id = records.size,
            record.properties,
        )
        table = table.copy(records = records)
        table.writeToFile(file)
    }

    fun deleteFirstWhere(
        type: String,
        value: String,
    ) {
        val indexOfType = table.types.indexOf(type)
        require(indexOfType != -1) { "no such type" }
        val indexOfRecord =
            table.records.indexOfFirst { record: ReadRecord -> record.properties[indexOfType] == value }
        require(indexOfRecord != -1) { "no such value" }
        table = table.copy(
            records = table.records.toMutableList().apply { removeAt(indexOfRecord) }
        )
        table.writeToFile(file)
    }

    fun drop() {
        table = table.copy(records = emptyList())
        table.writeToFile(file)
    }
}

data class Table(
    val name: String,
    val types: List<String>,
    val records: List<ReadRecord>,
)

class ReadRecord(
    val id: Int,
    val properties: List<String>,
)

class WriteRecord(
    val properties: List<String>,
)

fun main() {
    val dao = Dao("db/test.popa", "Test", listOf("first", "second"))
    dao.insert(
        WriteRecord(listOf("apple", "penis"))
    )
    dao.insert(
        WriteRecord(listOf("banana", "vagina"))
    )
    dao.drop()
}

private fun String.inBrackets(): String = substringAfter('[').substringBeforeLast(']')

private fun Table.writeToFile(file: File) {
    file.writeText(
        "[$name]\n"
    )
    file.appendText("[${types.reduce { acc, s -> "$acc|$s" }}]\n")
    records.forEach { prop: ReadRecord ->
        file.appendText("${prop.id}|")
        file.appendText(prop.properties.reduce { acc, s -> "$acc|$s" })
        file.appendText("\n")
    }
}
