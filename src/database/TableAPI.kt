package database

interface Table {
    val select: SelectableTable
    val delete: DeletableTable
    val insert: InsertableTable
    val update: UpdatableTable
}

interface SelectableTable {
    fun firstWhere(column: Column, value: Property): ReadRecord?
    fun allWhere(column: Column, value: Property): List<ReadRecord>
    fun all(): List<ReadRecord>
}

interface DeletableTable {
    fun firstWhere(column: Column, value: Property)
    fun allWhere(column: Column, value: Property)
    fun all()
}

interface InsertableTable {
    operator fun invoke(record: WriteRecord)
    fun all(records: List<WriteRecord>)
}

interface UpdatableTable {
    fun firstWhere(column: Column, value: Property, record: WriteRecord)
}

fun Table(
    path: String,
    name: String,
    columns: List<Column>,
): Table = object : Table {
    private val tableHelper = TableHelper(path, name, columns)

    override val select: SelectableTable = object : SelectableTable {
        override fun firstWhere(column: Column, value: Property): ReadRecord? =
            tableHelper.selectFirstWhere(column, value)

        override fun allWhere(column: Column, value: Property): List<ReadRecord> =
            tableHelper.selectAllWhere(column, value)

        override fun all(): List<ReadRecord> =
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
        override operator fun invoke(record: WriteRecord): Unit = tableHelper.insert(record)

        override fun all(records: List<WriteRecord>): Unit =
            tableHelper.insertAll(records)

    }

    override val update: UpdatableTable = object : UpdatableTable {
        override fun firstWhere(column: Column, value: Property, record: WriteRecord): Unit =
            tableHelper.updateFirstWhere(column, value, record)
    }
}
