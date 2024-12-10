package database

data class TableRepresentation(
    val name: String,
    val types: List<String>,
    val records: List<ReadRecord>,
)

data class ReadRecord(
    val id: Int,
    val properties: List<String>,
)

data class WriteRecord(
    val properties: List<String>,
)
