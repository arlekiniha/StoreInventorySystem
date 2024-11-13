fun main() {
    val array = intArrayOf(3, 2, 0, -6, 3)
    bubbleSort(array)
    println("RESULT: ${array.toList()}")
}

fun bubbleSort(array: IntArray) {
    while (true) {
        println(array.toList())
        var isSwitched = false
        for (i in 0 until array.size - 1) {
            if (array[i] > array[i + 1]) {
                val temp = array[i]
                array[i] = array[i + 1]
                array[i + 1] = temp
                isSwitched = true
            }
        }
        if (isSwitched.not()) break
    }
}