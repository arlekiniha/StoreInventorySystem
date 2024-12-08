package store_inventory_system

class Store(
    private val transactionManager: TransactionManager,
) {

    private val inventoryItems = mutableListOf<InventoryItem>()

    fun addProduct(inventoryItem: InventoryItem) {
        val existingInventoryItem = inventoryItems.find { it.name == inventoryItem.name }
        if (existingInventoryItem != null) {
            existingInventoryItem.restock(inventoryItem.stock)
            transactionManager.addStoreTransaction("You restocked ${inventoryItem.name} by ${inventoryItem.stock} units.")
            //TODO
        } else {
            inventoryItems.add(inventoryItem)
            transactionManager.addStoreTransaction("${inventoryItem.name} added to your store with ${inventoryItem.stock} units.")
            println("${inventoryItem.name} added to your store with ${inventoryItem.stock} units.")
        }
    }


    fun searchInventoryItem(name: String): InventoryItem? {
        return inventoryItems.find { it.name.lowercase() == name.lowercase() }
    }

    fun sellProduct(name: String, quantity: Int, totcost: Double) {
        val inventoryItem = searchInventoryItem(name)
        if (inventoryItem != null) {

            inventoryItem.toBuyAnItem(quantity)
            transactionManager.addUserTransaction("$quantity units of $name, $totcost$")
        } else {
            println("Product not available!")
        }
    }

    fun showAllProducts() {
        if (inventoryItems.isEmpty()) {
            println("No products in the inventory.")
        } else {
            val templist = inventoryItems.toMutableList()
            templist.removeIf { it.stock == 0 }
            for (product in templist) {
                println("${product.name} - Price: $${product.price}, Stock: ${product.stock}")
            }
        }
    }

    fun showUserTransactions() {
        println("Here are your all transactions:")
        transactionManager.getUserTransactions().forEach { println(it) }
    }

    fun showStoreTransactions() {
        println("Here are all store transactions")
        transactionManager.getStoreTransactions().forEach { println(it) }

    }

    fun chooseTransactionType() {
        println(
            """
            Choose which transactions you want to see: 
            1. User Transactions
            2. Store Transactions
        """.trimIndent()
        )
        when (readlnOrNull()?.toIntOrNull()) {
            1 -> showUserTransactions()
            2 -> showStoreTransactions()
        }
    }
}


