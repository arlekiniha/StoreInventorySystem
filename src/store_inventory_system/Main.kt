package store_inventory_system

fun main() {
    val store = Store()

    while (true) {
        println(
            """
                ==== Store Inventory System ====
                1. Add a Product
                2. Restock a Product
                3. Sell a Product
                4. Search for a Product
                5. List All Products
                6. Show yot Transactions
                7. Exit
                Choose an option:
            """.trimIndent()
        )

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> addItemToCatalog(store)
            2 -> restockItem(store)
            3 -> sellItem(store)
            4 -> searchItem(store)
            5 -> store.toShowAllProducts()
            6 -> transactionMaking(store)
            7 -> {
                println("Exiting the program.")
                break
            }

            else -> println("Invalid option! Please try again.")
        }
    }
}

private fun searchItem(store: Store) {
    print("Enter product name to search: ")
    val name = readlnOrNull()!!
    val product = store.searchInventoryItem(name)
    if (product != null) {
        println("${product.name} - Price: $${product.price}, Stock: ${product.stock}")
    } else {
        println("Product not found!")
    }
}

private fun sellItem(store: Store) {
    store.toShowAllProducts()
    print("Enter product name to sell: ")
    val name = readlnOrNull()!!
    val item = store.searchInventoryItem(name)
    if (item == null) {
        println("We don't have this item")
        return
    }
    print("Enter quantity to sell: ")
    val quantity = readlnOrNull()!!.toInt()
    val price = item.price
    val totcost = quantity * price
    store.sellProduct(name, quantity, totcost)
}

private fun restockItem(store: Store) {
    print("Enter product name to restock: ")
    val name = readlnOrNull()!!
    print("Enter restock quantity: ")
    val quantity = readlnOrNull()!!.toInt()
    val product = store.searchInventoryItem(name)
    if (product != null) {
        product.restock(quantity)
    } else {
        println("Product not found!")
    }
}

private fun addItemToCatalog(store: Store) {
    print("Enter product name: ")
    val name = readlnOrNull()!!
    print("Enter product price: ")
    val price = readlnOrNull()!!.toDoubleOrNull()
    if (price == null) {
        println("Product price should be number")
        return
    }
    print("Enter initial stock: ")
    val stock = readlnOrNull()!!.toIntOrNull()
    if (stock == null) {
        println("Initial stock should be number")
        return
    }
    store.addProduct(InventoryItem(name, price, stock))

}

fun transactionMaking(store: Store) {
    val item = store.transactions
    println("Here are your all transactions: \n $item")
}