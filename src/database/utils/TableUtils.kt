package database.utils

import database.*
import java.io.File

fun TableRepresentation.writeToFile(file: File) {
    file.writeText(
        "[$name]\n"
    )
    file.appendText("[${columns.map { "${it.name.value}:${it.type.asString()}:${it.primaryKey.pkAsLetter()}" }.reduce { acc, s -> "$acc|$s" }}]\n")
    records.forEach { prop: Record ->
        file.appendText(prop.properties.map { it.asString() }.reduce { acc, s -> "$acc|$s" })
        file.appendText("\n")
    }
}

fun List<Column>.findIndexOf(column: Column, lazyMessage: () -> Any = {}): Int =
    indexOfFirst { it == column }.also { require(it != -1, lazyMessage) }

fun String.nonEmptyLines() = lines().filter(String::isNotEmpty)

fun String.toTable(): TableRepresentation = nonEmptyLines().let { lines ->
    val columns = lines[1].columns
    TableRepresentation(
        name = lines[0].name,
        columns = columns,
        records = lines.drop(2).map { it.record(columns) }
    )
}

infix fun List<Property>.matches(columns: List<Column>) {
    require(size == columns.size)
    { "Provided record properties count are not the same as in table" }
    forEachIndexed { index, prop ->
        require(prop matches columns[index].type)
        { "Property type not matches with column type at index $index." }
    }
}

// todo check if fits
fun List<Record>.checkPrimaryKeyUniqueness(record: Record, columns: List<Column>) {
    val indexOfPrimaryKey = columns.indexOfFirst { it.primaryKey }
    map { r ->
        r.properties.filterIndexed { index, _ ->
            columns[index].primaryKey
        }.first()
    }.none { it == record.properties[indexOfPrimaryKey] }.also {
        require(it) { "Only unique values are allowed for primary key properties." }
    }
}

private val String.name: String
    get() = inBrackets()

private val String.columns: List<Column>
    get() = inBrackets().split('|').map {
        val (name, type, primaryKey) = it.split(':')
        require(primaryKey == "P" || primaryKey == "N")
        { "Column does not contain primary key property" }
        Column(name.lexeme, type.columnType, primaryKey.asPrimaryKey())
    }

fun String.asPrimaryKey(): Boolean = when (this) {
    "P" -> true
    "N" -> false
    else -> error("Is not primary key representation")
}

private fun String.record(columns: List<Column>): Record = split('|').let { props ->
    Record(
        properties = props.mapIndexed { index, r ->
            r.typed(columns[index].type)
        }
    )
}

fun listOfLexeme(vararg args: String): List<Lexeme> = args.map(::Lexeme)

fun String.inBrackets(): String =
    substringAfter('[').substringBeforeLast(']')
