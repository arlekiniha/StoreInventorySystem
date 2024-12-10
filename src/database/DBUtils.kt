package database

import java.io.File

fun TableRepresentation.writeToFile(file: File) {
    file.writeText(
        "[$name]\n"
    )
    file.appendText("[${types.reduce { acc, s -> "$acc|$s" }}]\n")
    records.forEach { prop: ReadRecord ->
        file.appendText("${prop.id}|")
        file.appendText(prop.properties.reduce { acc, s -> "$acc|$s" })
        file.appendText("\n")
    }
}

fun <T> List<T>.requireIndexOf(element: T, lazyMessage: () -> Any = {}): Int =
    indexOf(element).also { require(it != -1, lazyMessage) }

fun String.nonEmptyLines() = lines().filter(String::isNotEmpty)

fun String.toTable(): TableRepresentation = nonEmptyLines().let { lines ->
    TableRepresentation(
        name = lines[0].name,
        types = lines[1].types,
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
            properties = props.drop(1),
        )
    }

private fun String.inBrackets(): String =
    substringAfter('[').substringBeforeLast(']')
