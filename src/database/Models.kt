package database

data class TableRepresentation(
    val name: String,
    val types: List<Lexeme>,
    val records: List<ReadRecord>,
)

data class ReadRecord(
    val id: Int,
    val properties: List<Lexeme>,
)

@JvmInline
value class Lexeme(val value: String) {
    init {
        require(value.isLexemeSyntacticallyValid())
        { "Property is syntactically invalid: $value" }
    }
}

val String.prop
    get() = Lexeme(this)

val List<String>.props
    get() = map(String::prop)

val List<Lexeme>.strings
get() = map(Lexeme::value)

data class WriteRecord(
    val properties: List<Lexeme>,
)
