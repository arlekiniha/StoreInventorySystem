package store_inventory_system2.presentation

import store_inventory_system2.domain.Store

class Presenter(
    private val store: Store,
    private val view: View,
) {

    private var itemName: String = ""
    private var itemQuantity: Int = 0

    fun updateItemName(name: String) {
        itemName = name
    }

    fun updateItemQuantity(quantityInput: String) {
        val quantity = quantityInput.toIntOrNull()
        if(quantity == null) {
            view.showError()
            return
        }
        itemQuantity = quantity
    }

    fun buyItem() {
       val isBoughtSuccessfully = store.buyItem(itemName, itemQuantity)
        view.showBoughtSuccessfully(itemName, isBoughtSuccessfully)
    }

    // todo add other methods

}

class View {
    fun showBoughtSuccessfully(itemName: String, isSuccessful: Boolean) {
        if(isSuccessful) {
            println("You bought $itemName.")
        } else {
            println("You cannot buy $itemName.")
        }
    }

    // todo add specific error
    fun showError() {
        println("Something went wrong.")
    }
}