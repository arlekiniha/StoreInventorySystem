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