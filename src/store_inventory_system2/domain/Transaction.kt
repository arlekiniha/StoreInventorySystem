package store_inventory_system2.domain

data class Transaction (
    val message: String,
    val type: TransactionType
)

enum class TransactionType {
    User, Admin
}