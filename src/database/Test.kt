package database

import database.api.query
import database.api.record
import database.api.table


// todos
// string spaces representation
// check and refactor regexes
// check and refactor validation
// validate primary keys on init table
fun main() {
    val table = table {
        path = "db/test.opa"
        name = "test"
        columns("name") {
            "name".text
            "age".int
            "is_student".boolean
        }
    }

    table.insert(
        record { "Bob6" then 18 then false }
    )

    val result = table.select.allWhere(
        query { "is_student" eq false }
    )

    result.forEach { println(it) }
}
