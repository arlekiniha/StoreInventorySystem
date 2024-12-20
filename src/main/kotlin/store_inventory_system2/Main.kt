package store_inventory_system2

import store_inventory_system2.domain.RuntimeInventoryRepository
import store_inventory_system2.domain.RuntimeTransactionsRepository
import store_inventory_system2.domain.Store
import store_inventory_system2.domain.TestFlow
import store_inventory_system2.presentation.Presenter
import store_inventory_system2.presentation.View

fun main() {
    val transactionsRepository = RuntimeTransactionsRepository()
    val inventoryRepository = RuntimeInventoryRepository()
    val store = Store(
        inventoryRepository = inventoryRepository,
        transactionsRepository = transactionsRepository,
    )

    val view = View(inventoryRepository)
    val presenter = Presenter(
        store, view,
        transactionsRepository = transactionsRepository
    )
    view.initPresenter(presenter)
    val testFlow = TestFlow(store)

    while (true) {
        println(
            """
                ==== Store Inventory System ====
                0. Exit
                1. Add a Product
                2. Buy a Product
                3. Restock a Product
                4. Show transactions
                Choose an option:
            """.trimIndent()
        )

        when (readlnOrNull()?.toIntOrNull()) {
            0 -> {
                println("Exiting the program."); break
            }
            1 -> view.addItemInput()
            2 -> view.buyItemInput()
            3 -> view.restockItemInput()
            4 -> view.showTransactionsInput()
            9 -> testFlow.run {
                testAdd()
                testBuy()
            }

            else -> println("Invalid option! Please try again.")
        }
    }
}