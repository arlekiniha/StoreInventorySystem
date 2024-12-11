package database

import database.utils.*

data class TableRepresentation(
    val name: String,
    val columns: List<Column>,
    val records: List<ReadRecord>,
)

data class ReadRecord(
    val id: Int,
    val properties: List<Property>,
)

data class Column(
    val name: Lexeme,
    val type: ColumnType,
)

fun ColumnText(name: String) = Column(name.lexeme, ColumnType.STRING)
fun ColumnInt(name: String) = Column(name.lexeme, ColumnType.INT)
fun ColumnBoolean(name: String) = Column(name.lexeme, ColumnType.BOOLEAN)

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

fun Text(text: String) = Property.StringProperty(text) // todo check

fun Integer(number: Int) = Property.IntProperty(number) // todo check

fun Boolean(boolean: Boolean) = Property.BooleanProperty(boolean) // todo check

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

data class WriteRecord(
    val properties: List<Property>,
)
