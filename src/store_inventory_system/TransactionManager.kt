package store_inventory_system

class TransactionManager {
    private val transactions = mutableListOf<String>()

    fun add(transaction: String) {
        transactions += transaction
    }

    fun getTransactions(): List<String> {
        return transactions
    }
}