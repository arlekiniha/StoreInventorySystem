package database

// todos
// dsl
// ids are probably redundant or needs recalculation
// unique values
fun main() {
    val table = Table(
        path = "db/test.opa",
        name = "test",
        columns = listOf(
            ColumnText("name"),
            ColumnInt("age"),
            ColumnBoolean("is_student")
        ),
    )

//    val result = table.update.firstWhere(
//        ColumnInt("age"),
//        Integer(13),
//        WriteRecord(
//            listOf(
//                Text("Ivan"),
//                Integer(12),
//                Boolean(false),
//            )
//        )
//    )
//    println(result)
}
