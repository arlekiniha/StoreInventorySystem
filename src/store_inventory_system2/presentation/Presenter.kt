package store_inventory_system2.presentation

import store_inventory_system2.domain.Store

class Presenter(
    private val store: Store,
    private val view: View,
) {

    private var itemName: String = ""
    private var itemQuantity: Int = 0
    private var itemPrice: Int = 0

    fun updateItemName(name: String): Boolean {
        if (name.isBlank()) {
            view.showError()
            return false
        }
        itemName = name
        return true
    }

    fun updateItemQuantity(quantityInput: String): Boolean {
        val quantity = quantityInput.toIntOrNull()
        if (quantity == null || quantity <= 0) {
            view.showError()
            return false
        }
        itemQuantity = quantity
        return true
    }

    fun updateItemPrice(priceInput: String): Boolean {
        val price = priceInput.toIntOrNull()
        if (price == null) {
            view.showError()
            return false
        }
        itemPrice = price
        return true
    }

    fun buyItem() {
        val isBoughtSuccessfully = store.buyItem(itemName, itemQuantity)
        view.showBoughtItem(itemName, isBoughtSuccessfully)
        clear()
    }

    fun restockItem() {
        val isRestockedSuccessfully = store.restockItem(itemName, itemQuantity)
        view.showRestockedItem(itemName, itemQuantity, isRestockedSuccessfully)
        clear()
    }

    fun addItem(){
        val isAddedSuccessfully = store.addItem(itemName, itemPrice, itemQuantity)
        view.showAddedItem(itemName, isAddedSuccessfully)
        clear()
    }

    private fun clear() {
        itemName = ""
        itemQuantity = 0
        itemPrice = 0
    }

}

//todo move view to another file

class View {

    private lateinit var presenter: Presenter

    fun initPresenter(presenter: Presenter) {
        this.presenter = presenter
    }

    fun buyItemInput() {
        while (true) {
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

    // todo add specific error
    fun showError() {
        println("Something went wrong.")
    }
}