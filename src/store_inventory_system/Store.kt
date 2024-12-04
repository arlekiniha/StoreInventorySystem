package store_inventory_system

class Store {
    val inventoryItems = mutableListOf<InventoryItem>()
    public val transactions = mutableListOf<String>()

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
            transactions.add("$quantity units of $name, $totcost$ \n")
        } else {
            println("Product not available!")
        }
    }

    fun toShowAllProducts() {
        if (inventoryItems.isEmpty()) {
            println("No products in the inventory.")
        } else {
            val templist = inventoryItems
            templist.removeIf { it.stock == 0 }
            for (product in templist) {
                println("${product.name} - Price: $${product.price}, Stock: ${product.stock}")
            }
        }
    }

}

