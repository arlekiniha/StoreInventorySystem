package store_inventory_system2.domain

interface TransactionsRepository {
    fun addTransaction(transaction: Transaction)
    fun allTransactions(): List<Transaction>
}

class RuntimeTransactionsRepository : TransactionsRepository {

    private val transactions = mutableListOf<Transaction>()

    override fun addTransaction(transaction: Transaction) {
        transactions += transaction
    }

    override fun allTransactions(): List<Transaction> {
        return transactions
    }

    fun getUserTransactions() {
        for (transaction in transactions) {
            if (transaction.type == TransactionType.User) {
                println(transaction.message)
            }
        }
    }

    fun getAdminTransactions() {
        for (transaction in transactions) {
            if (transaction.type == TransactionType.Admin)
                println(transaction.message)
        }
    }
}

class TestTransactionsRepository : TransactionsRepository {
    override fun addTransaction(transaction: Transaction) {

    }

    override fun allTransactions(): List<Transaction> {
        return listOf(
            Transaction("Test admin transaction add", TransactionType.Admin),
            Transaction("Test user transaction add", TransactionType.User),
        )
    }
}