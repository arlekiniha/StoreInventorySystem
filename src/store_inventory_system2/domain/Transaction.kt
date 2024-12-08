package store_inventory_system2.domain

class Transaction (
    val message: String,
    val type: TransactionType
)

enum class TransactionType {
    User, Admin
}