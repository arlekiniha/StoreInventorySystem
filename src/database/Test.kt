package database


// todos
// dsl
// unique values additionals
// string spaces representation
// check and refactor regexes
// check and refactor validation
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
        record { "Bob3" then 18 then false }
    )

    val result = table.select.allWhere(
        query { "is_student" eq false }
    )

    result.forEach { println(it) }
}
