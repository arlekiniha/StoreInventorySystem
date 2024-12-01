package store_inventory_system

class Store {
    private val products = mutableListOf<Product>()

    fun addProduct(product: Product) {
        val existingProduct = products.find { it.name == product.name }
        if (existingProduct != null) {
            existingProduct.restock(product.stock)
        } else {
            products.add(product)
            println("${product.name} added to the inventory.")
        }
    }

    fun searchProduct(name: String): Product? {
        return products.find { it.name.equals(name, ignoreCase = true) }
    }

    fun sellProduct(name: String, quantity: Int) {
        val product = searchProduct(name)
        if (product != null) {
            product.buy(quantity)
        } else {
            println("Product not available!")
        }
    }

    fun listAllProducts() {
        if (products.isEmpty()) {
            println("No products in the inventory.")
        } else {
            println("Available products:")
            for (product in products) {
                println("${product.name} - Price: $${product.price}, Stock: ${product.stock}")
            }
        }
    }
}
