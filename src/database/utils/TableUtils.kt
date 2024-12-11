package database.utils

import database.*
import java.io.File

fun TableRepresentation.writeToFile(file: File) {
    file.writeText(
        "[$name]\n"
    )
    file.appendText("[${types.strings.reduce { acc, s -> "$acc|$s" }}]\n")
    records.forEach { prop: ReadRecord ->
        file.appendText("${prop.id}|")
        file.appendText(prop.properties.strings.reduce { acc, s -> "$acc|$s" })
        file.appendText("\n")
    }
}

fun <T> List<T>.requireIndexOf(element: T, lazyMessage: () -> Any = {}): Int =
    indexOf(element).also { require(it != -1, lazyMessage) }

fun String.nonEmptyLines() = lines().filter(String::isNotEmpty)

fun String.toTable(): TableRepresentation = nonEmptyLines().let { lines ->
    TableRepresentation(
        name = lines[0].name,
        types = lines[1].types.props,
        records = lines.drop(2).map { it.record }
    )
}

private val String.name: String
    get() = inBrackets()

private val String.types: List<String>
    get() = inBrackets().split('|')

private val String.record: ReadRecord
    get() = split('|').let { props ->
        ReadRecord(
            id = props.first().toInt(),
            properties = props.drop(1).props,
        )
    }

fun listOfLexeme(vararg args: String): List<Lexeme> = args.map(::Lexeme)

fun String.inBrackets(): String =
    substringAfter('[').substringBeforeLast(']')
