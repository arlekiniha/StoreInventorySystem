package store_inventory_system

class TransactionManager {
    private val userTransactions = mutableListOf<String>()
    private val storeTransactions = mutableListOf<String>()


    fun addUserTransaction(transaction: String) {
        userTransactions += transaction
    }

    fun addStoreTransaction(transaction: String) {
        storeTransactions += transaction
    }

    fun getUserTransactions(): List<String> {
        return userTransactions
    }

    fun getStoreTransactions(): List<String> {
        return storeTransactions
    }

}