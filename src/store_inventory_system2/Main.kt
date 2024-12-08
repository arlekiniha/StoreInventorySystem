package store_inventory_system2

import store_inventory_system2.domain.RuntimeInventoryRepository
import store_inventory_system2.domain.RuntimeTransactionsRepository
import store_inventory_system2.domain.Store
import store_inventory_system2.presentation.Presenter
import store_inventory_system2.presentation.View

fun main() {
    val transactionsRepository = RuntimeTransactionsRepository()
    val inventoryRepository = RuntimeInventoryRepository()
    val store = Store(
        inventoryRepository = inventoryRepository,
        transactionsRepository = transactionsRepository,
    )

    val view = View()
    val presenter = Presenter(store, view)

    while (true) {
        println(
            """
                ==== Store Inventory System ====
                1. Buy a Product
                2. Exit
                Choose an option:
            """.trimIndent()
        )

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                val itemName = readln()
                presenter.updateItemName(itemName)
                val itemQuantity = readln()
                presenter.updateItemQuantity(itemQuantity)
                presenter.buyItem()
            }
            2 -> {
                println("Exiting the program.")
                break
            }

            else -> println("Invalid option! Please try again.")
        }
    }
}