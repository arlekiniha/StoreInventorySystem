package store_inventory_system

class InventoryItem(
    val name: String,
    val price: Double,
    var stock: Int,
    private val transactionManager: TransactionManager,
) {


    fun toBuyAnItem(quantity: Int) {
        if (quantity <= 0) {
            println("You can not buy negative or 0 amount of items")
            return
        }
        if (stock >= quantity) {
            stock -= quantity
            println("You bought $quantity of $name")
        } else {
            println("Not enough stock available")
        }
    }

    fun restock(quantity: Int) {
        if (quantity >= 0) {
            stock += quantity
            transactionManager.addStoreTransaction("You restocked $quantity units of $name")
            println("$name restocked by $quantity. Current stock: $stock")
        } else {
            println("You cannot restock by negative amount of items")
        }
    }

}