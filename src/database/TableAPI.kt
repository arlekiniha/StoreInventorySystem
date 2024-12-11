package database

interface Table {
    val select: SelectableTable
    val delete: DeletableTable
    val insert: InsertableTable
    val update: UpdatableTable
}

interface SelectableTable {
    fun firstWhere(column: Column, value: Property): Record?
    fun firstWhere(query: QueryEntry): Record? = firstWhere(query.column, query.property)
    fun allWhere(column: Column, value: Property): List<Record>
    fun allWhere(query: QueryEntry): List<Record> = allWhere(query.column, query.property)
    fun all(): List<Record>
}

data class QueryEntry(
    val column: Column,
    val property: Property,
)

class QueryEntryScope internal constructor() {

    var property: Property? = null
    var column: Column? = null

    infix fun String.eq(value: String) {
        column = columnText(this)
        property = text(value)
    }

    infix fun String.eq(value: Int) {
        column = columnInteger(this)
        property = integer(value)
    }

    infix fun String.eq(value: Boolean) {
        column = columnBoolean(this)
        property = boolean(value)
    }

    fun toQueryEntry(): QueryEntry {
        val col = requireNotNull(column)
        { "Column is not specified" }
        val prop = requireNotNull(property) { "Property is not specified" }
        return QueryEntry(col, prop)
    }
}

fun query(query: QueryEntryScope.() -> Unit): QueryEntry =
    QueryEntryScope().apply(query).toQueryEntry()

interface DeletableTable {
    fun firstWhere(column: Column, value: Property)
    fun allWhere(column: Column, value: Property)
    fun all()
}

interface InsertableTable {
    operator fun invoke(record: Record)
    fun all(records: List<Record>)
}

interface UpdatableTable {
    fun firstWhere(column: Column, value: Property, record: Record)
}

fun Table(
    path: String,
    name: String,
    columns: Columns,
): Table = object : Table {
    private val tableHelper = TableHelper(path, name, columns.value)

    override val select: SelectableTable = object : SelectableTable {
        override fun firstWhere(column: Column, value: Property): Record? =
            tableHelper.selectFirstWhere(column, value)

        override fun allWhere(column: Column, value: Property): List<Record> =
            tableHelper.selectAllWhere(column, value)

        override fun all(): List<Record> =
            tableHelper.selectAll()
    }


    override val delete: DeletableTable = object : DeletableTable {
        override fun firstWhere(column: Column, value: Property): Unit =
            tableHelper.deleteFirstWhere(column, value)

        override fun allWhere(column: Column, value: Property): Unit =
            tableHelper.deleteAllWhere(column, value)

        override fun all(): Unit = tableHelper.deleteAll()

    }

    override val insert: InsertableTable = object : InsertableTable {
        override operator fun invoke(record: Record): Unit = tableHelper.insert(record)

        override fun all(records: List<Record>): Unit =
            tableHelper.insertAll(records)

    }

    override val update: UpdatableTable = object : UpdatableTable {
        override fun firstWhere(column: Column, value: Property, record: Record): Unit =
            tableHelper.updateFirstWhere(column, value, record)
    }
}

