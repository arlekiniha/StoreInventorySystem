package store_inventory_system2.presentation

import store_inventory_system2.domain.InventoryRepository

class View(private val inventoryRepository: InventoryRepository) {

    private lateinit var presenter: Presenter

    fun initPresenter(presenter: Presenter) {
        this.presenter = presenter
    }

    fun buyItemInput() {
        while (true) {
            showAllProducts()
            println("Enter product name: (print back to return)")
            val productNameInput = readln()
            if (productNameInput.lowercase() == "back") break
            val isItemNameUpdatedSuccessfully = presenter.updateItemName(productNameInput)
            if (isItemNameUpdatedSuccessfully.not()) continue
            println("Enter product quantity: (print back to return)")
            val productQuantityInput = readln()
            if (productQuantityInput.lowercase() == "back") break
            if (presenter.updateItemQuantity(productQuantityInput).not()) continue
            presenter.buyItem()
            break
        }
    }

    fun restockItemInput() {
        while (true) {
            println("Enter product name: (print back to return)")
            val productNameInput = readln()
            if (productNameInput.lowercase() == "back") break
            val isItemNameUpdatedSuccessfully = presenter.updateItemName(productNameInput)
            if (isItemNameUpdatedSuccessfully.not()) continue
            println("Enter quantity to restock: (print back to return)")
            val productQuantityInput = readln()
            if (productQuantityInput.lowercase() == "back") break
            if (presenter.updateItemQuantity(productQuantityInput).not()) continue
            presenter.restockItem()
            break
        }
    }

    fun addItemInput() {
        while (true) {
            println("Enter product name: (print back to return)")
            val productNameInput = readln()
            if (productNameInput.lowercase() == "back") break
            val isItemNameUpdatedSuccessfully = presenter.updateItemName(productNameInput)
            if (isItemNameUpdatedSuccessfully.not()) continue
            println("Enter product price: (print back to return)")
            val productPriceInput = readln()
            val isItemPriceUpdatedSuccessfully = presenter.updateItemPrice(productPriceInput)
            if (isItemPriceUpdatedSuccessfully.not()) continue
            println("Enter initial quantity: (print back to return)")
            val productQuantityInput = readln()
            if (productQuantityInput.lowercase() == "back") break
            if (presenter.updateItemQuantity(productQuantityInput).not()) continue
            presenter.addItem()
            break
        }
    }

    fun showTransactionsInput() {
        while (true) {
            println("Choose transactions type: (admin or user)")
            val input = readln()
            if (input.lowercase() == "admin") {
                presenter.getAdminTransactions()
                return
            } else if (input.lowercase() == "user") {
                presenter.getUserTransactions()
                return
            } else {
                inputError()
                return
            }
        }
    }

    fun showBoughtItem(itemName: String, isSuccessful: Boolean) {
        if (isSuccessful) {
            println("You bought $itemName.")
        } else {
            println("You cannot buy $itemName.")
        }
    }

    fun showRestockedItem(itemName: String, quantity: Int, isSuccessful: Boolean) {
        if (isSuccessful) {
            println("You restocked $itemName by $quantity")
        } else {
            println("You cannot restock $itemName by $quantity")
        }
    }

    fun showAddedItem(itemName: String, isSuccessful: Boolean) {
        if (isSuccessful) {
            println("You added $itemName")
        } else {
            println("You cannot add $itemName")
        }
    }

    private fun showAllProducts() {
        for (item in inventoryRepository.allItems()) {
            val name = item.name
            val price = item.price
            val quantity = item.quantity
            println("$name, $price$, $quantity units")
        }
    }

    fun showError() {
        println("Something went wrong.")
    }

    fun inputError() {
        println("Invalid input.")
    }

    fun outputError() {
        println("We can't do it")
    }
}