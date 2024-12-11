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
            view.inputError()
            return false
        }
        itemName = name
        return true
    }

    fun updateItemQuantity(quantityInput: String): Boolean {
        val quantity = quantityInput.toIntOrNull()
        if (quantity == null || quantity <= 0) {
            view.inputError()
            return false
        }
        itemQuantity = quantity
        return true
    }

    fun updateItemPrice(priceInput: String): Boolean {
        val price = priceInput.toIntOrNull()
        if (price == null) {
            view.inputError()
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
