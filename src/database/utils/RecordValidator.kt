package database.utils

fun String.isRecordSyntacticallyValid(): Boolean =
    matches("^(?:[a-zA-Z0-9]+(?:\\|[a-zA-Z0-9]+)*)?\$".toRegex())


fun String.isLexemeSyntacticallyValid(): Boolean =
    matches("^[a-zA-Z0-9_-]+\$".toRegex())
