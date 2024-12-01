package store_inventory_system

class Store {
    private val inventoryItems = mutableListOf<InventoryItem>()

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

    fun sellProduct(name: String, quantity: Int) {
        val inventoryItem = searchInventoryItem(name)
        if (inventoryItem != null) {
            inventoryItem.buy(quantity)
        } else {
            println("Product not available!")
        }
    }

    fun listAllProducts() {
        if (inventoryItems.isEmpty()) {
            println("No products in the inventory.")
        } else {
            println("Available products:")
            for (product in inventoryItems) {
                println("${product.name} - Price: $${product.price}, Stock: ${product.stock}")
            }
        }
    }
}
