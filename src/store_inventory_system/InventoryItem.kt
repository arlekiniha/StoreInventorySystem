package store_inventory_system

class InventoryItem(val name: String, val price: Double, var stock: Int) {


    fun toBuyAnItem(quantity: Int) {
        if (quantity <= 0){
            println("Ypu can not buy negative or 0 amount of items")
        }
        if (stock >= quantity) {
            stock -= quantity
            println("You bought $quantity of $name")
        } else {
            println("Not enough stock available")
        }
    }

    fun restock(quantity: Int) {
        stock += quantity
        println("$name restocked by $quantity. Current stock: $stock")
    }

}