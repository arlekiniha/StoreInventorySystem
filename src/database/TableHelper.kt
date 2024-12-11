package database

import database.utils.OpaValidator
import database.utils.toTable
import database.utils.writeToFile
import java.io.File

class TableHelper(
    path: String,
    tableName: String,
    tableTypes: List<Column>,
) {
    private val file = File(path)
    private val validator: OpaValidator = OpaValidator(file, tableTypes)
    private var table = TableRepresentation(tableName, tableTypes, emptyList())

    init {
        require(tableTypes.isNotEmpty()) { "Table must have at least one type" }
        if (validator.isRecordsValid().not()) transaction { table }
        else table = file.readText().toTable()
    }

    fun transaction(block: (currentTable: TableRepresentation) -> TableRepresentation, ) {
        table = block(table)
        table.writeToFile(file)
    }

    fun <T> provide(block: (currentTable: TableRepresentation) -> T): T = block(table)
}

