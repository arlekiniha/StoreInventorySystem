package store_inventory_system

fun main() {
    val store = Store()

    while (true) {
        println("\n==== Store Inventory System ====")
        println("1. Add a Product")
        println("2. Restock a Product")
        println("3. Sell a Product")
        println("4. Search for a Product")
        println("5. List All Products")
        println("6. Exit")
        print("Choose an option: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                print("Enter product name: ")
                val name = readlnOrNull()!!
                print("Enter product price: ")
                val price = readlnOrNull()!!.toDouble()
                print("Enter initial stock: ")
                val stock = readlnOrNull()!!.toInt()
                store.addProduct(Product(name, price, stock))
            }

            2 -> {
                print("Enter product name to restock: ")
                val name = readlnOrNull()!!
                print("Enter restock quantity: ")
                val quantity = readlnOrNull()!!.toInt()
                val product = store.searchProduct(name)
                if (product != null) {
                    product.restock(quantity)
                } else {
                    println("Product not found!")
                }
            }

            3 -> {
                print("Enter product name to sell: ")
                val name = readlnOrNull()!!
                print("Enter quantity to sell: ")
                val quantity = readlnOrNull()!!.toInt()
                store.sellProduct(name, quantity)
            }

            4 -> {
                print("Enter product name to search: ")
                val name = readlnOrNull()!!
                val product = store.searchProduct(name)
                if (product != null) {
                    println("${product.name} - Price: $${product.price}, Stock: ${product.stock}")
                } else {
                    println("Product not found!")
                }
            }

            5 -> store.listAllProducts()
            6 -> {
                println("Exiting the program.")
                break
            }

            else -> println("Invalid option! Please try again.")
        }
    }
}