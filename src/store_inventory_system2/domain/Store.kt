package store_inventory_system2.domain

//todo add other transactions
class Store(
    private val inventoryRepository: InventoryRepository,
    private val transactionsRepository: TransactionsRepository,
) {
    fun buyItem(itemName: String, quantity: Int): Boolean {
        if(quantity <= 0) return false
        val existingItem = inventoryRepository.getItem(itemName) ?: return false
        if(existingItem.quantity < quantity) return false

        val isReplaced = inventoryRepository.replaceItem(
            itemName,
            InventoryItem(
                itemName,
                price = existingItem.price,
                quantity = existingItem.quantity - quantity,
            )
        )
        transactionsRepository.addTransaction(
            transaction = Transaction("You bought $quantity units of $itemName", TransactionType.User)
        )
        return isReplaced
    }

    fun addItem(itemName: String, itemPrice: Int, quantity: Int): Boolean {
        val item = InventoryItem(itemName, itemPrice, quantity)
        return inventoryRepository.addItem(item)
    }

    fun searchItem(itemName: String): InventoryItem? {
        return inventoryRepository.getItem(itemName)
    }

    /**
     * If no item in repository with name itemName, no item will be added and method returns false
     */
    fun restockItem(itemName: String, quantity: Int): Boolean {
        if(quantity <= 0) return false
        val existingItem = inventoryRepository.getItem(itemName) ?: return false

        val isReplaced = inventoryRepository.replaceItem(
            itemName,
            InventoryItem(
                itemName,
                price = existingItem.price,
                quantity = existingItem.quantity + quantity,
            )
        )

        return isReplaced
    }

}