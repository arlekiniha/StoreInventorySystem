package database

// when providing existing table with wrong types no exception thrown
// no validation for input types and no validation for empty value on read
fun main() {
    val table = Table(
        path = "db/test.opa",
        name = "test",
        types = listOf("first", "second"),
        validateRecords = true,
    )
    val result = table.selectAll()
    println(result)
}
