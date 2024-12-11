package database

fun table(
    builder: TableScope.() -> Unit,
): Table = TableScope().apply(builder).toTable()


class TableScope {
    var path: String? = null

    var name: String? = null

    private var columns: Columns? = null

    fun columns(primaryKey: String, block: ColumnScope.() -> Unit) {
        columns = columnsBuilder(primaryKey, block)
    }

    internal fun toTable(): Table {
        val p = requireNotNull(path) { "Path should be defined" }
        require(p.isNotBlank()) { { "Path should be not blank" } }

        val n = requireNotNull(name) { "Name should be defined" }
        require(n.isNotBlank()) { { "Name should be not blank" } }

        val c = requireNotNull(columns)
        { "Columns should be defined" }
        return Table(p, n, c)
    }
}
