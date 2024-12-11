package database.utils

import database.*
import java.io.File

fun TableRepresentation.writeToFile(file: File) {
    file.writeText(
        "[$name]\n"
    )
    file.appendText("[${columns.map { "${it.name.value}:${it.type.asString()}" }.reduce { acc, s -> "$acc|$s" }}]\n")
    records.forEach { prop: ReadRecord ->
        file.appendText("${prop.id}|")
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

private val String.name: String
    get() = inBrackets()

private val String.columns: List<Column>
    get() = inBrackets().split('|').map {
        val (name, type) = it.split(':')
        Column(name.lexeme, type.columnType)
    }

private fun String.record(columns: List<Column>): ReadRecord = split('|').let { props ->
    ReadRecord(
        id = props.first().toInt(),
        properties = props.drop(1).mapIndexed { index, r ->
            r.typed(columns[index].type)
        }
    )
}

fun listOfLexeme(vararg args: String): List<Lexeme> = args.map(::Lexeme)

fun String.inBrackets(): String =
    substringAfter('[').substringBeforeLast(']')
