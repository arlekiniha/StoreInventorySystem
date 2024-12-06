package store_inventory_system

class Store {
    private val inventoryItems = mutableListOf<InventoryItem>()
    private val transactionManager = TransactionManager()

    fun addProduct(inventoryItem: InventoryItem) {
        val existingInventoryItem = inventoryItems.find { it.name == inventoryItem.name }
        if (existingInventoryItem != null) {
            existingInventoryItem.restock(inventoryItem.stock)
        } else {
            inventoryItems.add(inventoryItem)
            println("${inventoryItem.name} added to the inventory.")
        }
    } 

    fun searchInventoryItem(name: String): InventoryItem? {
        return inventoryItems.find { it.name.lowercase() == name.lowercase() }
    }

    fun sellProduct(name: String, quantity: Int, totcost: Double) {
        val inventoryItem = searchInventoryItem(name)
        if (inventoryItem != null) {

            inventoryItem.toBuyAnItem(quantity)
            transactionManager.add("$quantity units of $name, $totcost$")
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

    fun showTransactions() {
        println("Here are your all transactions:")
        transactionManager.getTransactions().forEach { println(it) }
    }
}

