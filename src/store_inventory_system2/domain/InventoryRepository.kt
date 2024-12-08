package store_inventory_system2.domain

interface InventoryRepository {
    fun addItem(item: InventoryItem): Boolean
    /**
     * If no item in repository with name itemName, no item will be added and method returns false
     */
    fun replaceItem(itemName: String, newItem: InventoryItem): Boolean
    fun removeItem(itemName: String): Boolean
    fun allItems(): List<InventoryItem>
    fun getItem(itemName: String): InventoryItem?
}

class RuntimeInventoryRepository : InventoryRepository {
    private val inventoryItems = mutableListOf<InventoryItem>()

    override fun addItem(item: InventoryItem): Boolean {
        inventoryItems += item
        return true
    }

    override fun replaceItem(itemName: String, newItem: InventoryItem): Boolean {
        if(inventoryItems.none { item -> item.name == itemName }) return false

        val indexOfItem = inventoryItems.indexOfFirst { it.name == itemName }
        inventoryItems[indexOfItem] = newItem
        return true
    }


    override fun removeItem(itemName: String): Boolean {
        return inventoryItems.removeIf { item: InventoryItem -> item.name == itemName }
    }

    override fun allItems(): List<InventoryItem> {
        return inventoryItems
    }

    override fun getItem(itemName: String): InventoryItem? {
        return inventoryItems.firstOrNull { it.name == itemName }
    }
}

// TODO add test InventoryRepository