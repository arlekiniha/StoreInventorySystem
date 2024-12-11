package database

import database.utils.listOfLexeme

fun main() {
    val table = Table(
        path = "db/test.opa",
        name = "test",
        types = listOfLexeme("first", "second"),
    )
}
