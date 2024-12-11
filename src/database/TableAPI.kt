package database

interface Table {
    val select: SelectableTable
    val delete: DeletableTable
    val insert: InsertableTable
    val update: UpdatableTable
}

interface SelectableTable {
    fun firstWhere(type: Lexeme, value: Lexeme): ReadRecord?
    fun allWhere(type: Lexeme, value: Lexeme): List<ReadRecord>
    fun all(): List<ReadRecord>
}

interface DeletableTable {
    fun firstWhere(type: Lexeme, value: Lexeme)
    fun allWhere(type: Lexeme, value: Lexeme)
    fun all()
}

interface InsertableTable {
    operator fun invoke(record: WriteRecord)
    fun all(records: List<WriteRecord>)
}

interface UpdatableTable {
    fun firstWhere(type: Lexeme, value: Lexeme, record: WriteRecord)
}

fun Table(
    path: String,
    name: String,
    types: List<Lexeme>,
): Table = object : Table {
    private val tableHelper = TableHelper(path, name, types)

    override val select: SelectableTable = object : SelectableTable {
        override fun firstWhere(type: Lexeme, value: Lexeme): ReadRecord? =
            tableHelper.selectFirstWhere(type, value)

        override fun allWhere(type: Lexeme, value: Lexeme): List<ReadRecord> =
            tableHelper.selectAllWhere(type, value)

        override fun all(): List<ReadRecord> =
            tableHelper.selectAll()
    }


    override val delete: DeletableTable = object : DeletableTable {
        override fun firstWhere(type: Lexeme, value: Lexeme): Unit =
            tableHelper.deleteFirstWhere(type, value)

        override fun allWhere(type: Lexeme, value: Lexeme): Unit =
            tableHelper.deleteAllWhere(type, value)

        override fun all(): Unit = tableHelper.deleteAll()

    }

    override val insert: InsertableTable = object : InsertableTable {
        override operator fun invoke(record: WriteRecord): Unit = tableHelper.insert(record)

        override fun all(records: List<WriteRecord>): Unit =
            tableHelper.insertAll(records)

    }

    override val update: UpdatableTable = object : UpdatableTable {
        override fun firstWhere(type: Lexeme, value: Lexeme, record: WriteRecord): Unit =
            tableHelper.updateFirstWhere(type, value, record)
    }
}
