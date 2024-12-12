package store_inventory_system2.domain

class TestFlow(
    private val store: Store
) {
    fun testAdd() {
        store.addItem("apple", 3, 10)
        store.addItem("pear", 5, 15)
    }

    fun testBuy() {
        store.buyItem("apple", 5)
        store.buyItem("pear", 2)
    }
}