package database

import database.utils.*

data class TableRepresentation(
    val name: String,
    val columns: List<Column>,
    val records: List<Record>,
)

@JvmInline
value class Record internal constructor(
    val properties: List<Property>,
)

class RecordScope internal constructor() {
    private val properties = mutableListOf<Property>()

    val String.add: Unit
        get() {
            properties += text(this)
        }

    val Int.add: Unit
        get() {
            properties += integer(this)
        }

    val Boolean.add: Unit
        get() {
            properties += boolean(this)
        }

    infix fun String.then(prop: Int): Property {
        val property = text(this)
        val property2 = integer(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun String.then(prop: String): Property {
        val property = text(this)
        val property2 = text(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun String.then(prop: Boolean): Property {
        val property = text(this)
        val property2 = boolean(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun Boolean.then(prop: Int): Property {
        val property = boolean(this)
        val property2 = integer(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun Boolean.then(prop: String): Property {
        val property = boolean(this)
        val property2 = text(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun Boolean.then(prop: Boolean): Property {
        val property = boolean(this)
        val property2 = boolean(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun Int.then(prop: Int): Property {
        val property = integer(this)
        val property2 = integer(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun Int.then(prop: String): Property {
        val property = integer(this)
        val property2 = text(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun Int.then(prop: Boolean): Property {
        val property = integer(this)
        val property2 = boolean(prop)
        properties += property
        properties += property2
        return property2
    }

    infix fun Property.then(prop: String): Property {
        val property = text(prop)
        properties += property
        return property
    }

    infix fun Property.then(prop: Int): Property {
        val property = integer(prop)
        properties += property
        return property
    }

    infix fun Property.then(prop: Boolean): Property {
        val property = boolean(prop)
        properties += property
        return property
    }

    internal fun toColumns() = Record(properties)
}

fun record(builder: RecordScope.() -> Unit): Record =
    RecordScope().apply(builder).toColumns()

operator fun Property.plus(property: Property): Record = Record(listOf(this, property))
operator fun Record.plus(property: Property): Record = Record(properties + listOf(property))

@JvmInline
value class Columns internal constructor(val value: List<Column>)

data class Column(
    val name: Lexeme,
    val type: ColumnType,
    val primaryKey: Boolean,
)

class ColumnScope internal constructor() {
    private val columns = mutableListOf<Column>()

    val String.int: Unit
        get() {
            columns += columnInteger(this)
        }

    val String.text: Unit
        get() {
            columns += columnText(this)
        }

    val String.boolean: Unit
        get() {
            columns += columnBoolean(this)
        }

    internal fun toColumns() = Columns(columns)
}

//todo rename
internal fun columnsBuilder(primaryKey: String, block: ColumnScope.() -> Unit): Columns =
    ColumnScope().apply(block).toColumns()
        .value
        .also {
            require(it.isNotEmpty()) { "Table columns should contains at least one column" }
        }
        .map { column ->
            if (column.name.value == primaryKey) Column(column.name, column.type, true)
            else column
        }.let {
            require(it.count(Column::primaryKey) == 1)
            { "Table columns should contains exactly one primary key" }
            Columns(it)
        }

// todo rename
fun Boolean.pkAsLetter() = if (this) "P" else "N"

fun columnText(name: String, primaryKey: Boolean = false) = Column(name.lexeme, ColumnType.STRING, primaryKey)
fun columnInteger(name: String, primaryKey: Boolean = false) = Column(name.lexeme, ColumnType.INT, primaryKey)
fun columnBoolean(name: String, primaryKey: Boolean = false) = Column(name.lexeme, ColumnType.BOOLEAN, primaryKey)

enum class ColumnType {
    INT,
    STRING,
    BOOLEAN;

    fun asString(): String = when (this) {
        INT -> "INTEGER"
        STRING -> "STRING"
        BOOLEAN -> "BOOLEAN"
    }
}

val String.columnType: ColumnType
    get() = when (this) {
        "INTEGER" -> ColumnType.INT
        "STRING" -> ColumnType.STRING
        "BOOLEAN" -> ColumnType.BOOLEAN
        else -> error("Column Type is invalid")
    }

fun String.typed(columnType: ColumnType): Property =
    when (columnType) {
        ColumnType.INT -> {
            require(isIntSyntacticallyValid())
            { "Integer property is syntactically invalid: $this" }
            Property.IntProperty(toInt()) // todo check
        }

        ColumnType.STRING -> {
            require(isStringSyntacticallyValid())
            { "String property is syntactically invalid: $this" }
            Property.StringProperty(this.replace("?", " ")) // todo check
        }

        ColumnType.BOOLEAN -> {
            require(isBooleanSyntacticallyValid())
            { "Boolean property is syntactically invalid: $this" }
            Property.BooleanProperty(this.asBoolean())
        }
    }

fun text(text: String) = Property.StringProperty(text) // todo check

fun integer(number: Int) = Property.IntProperty(number) // todo check

fun boolean(boolean: Boolean) = Property.BooleanProperty(boolean) // todo check

sealed interface Property {

    data class IntProperty(val value: Int) : Property

    data class StringProperty(val value: String) : Property

    data class BooleanProperty(val value: Boolean) : Property

    infix fun matches(columnType: ColumnType): Boolean = when (columnType) {
        ColumnType.INT -> this is IntProperty
        ColumnType.STRING -> this is StringProperty
        ColumnType.BOOLEAN -> this is BooleanProperty
    }

    fun asString() = when (this) {
        is IntProperty -> this.value.toString()
        is StringProperty -> this.value.replace(" ", "?") // todo check
        is BooleanProperty -> when (this.value) {
            true -> "TRUE"
            false -> "FALSE"
        }
    }

}

@JvmInline
value class Lexeme(val value: String) {
    init {
        require(value.isLexemeSyntacticallyValid())
        { "Property is syntactically invalid: $value" }
    }
}

val String.lexeme
    get() = Lexeme(this)

val List<String>.lexemes
    get() = map(String::lexeme)

val List<Lexeme>.strings
    get() = map(Lexeme::value)
