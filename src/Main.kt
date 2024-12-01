import kotlin.random.Random


/* fun main() {
    val array = intArrayOf(3, 1, 0, 5, -4, 45)
    //change fun name to check different sorts methods(next line)
    quicksort(array)
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

fun quicksort(array: IntArray, start: Int = 0, end: Int = array.size - 1){
    if (end <= start) return
    val pivot = array[end]
    var insertIndex = start
    for (i in start..<end){
        if(array[i] <= pivot){
            val temp = array[i]
            array[i] = array[insertIndex]
            array[insertIndex] = temp
            insertIndex++
        }
    }
    val temp = array[end]
    array[end] = array[insertIndex]
    array[insertIndex] = temp

    quicksort(array, start, insertIndex - 1,)
    quicksort(array, insertIndex + 1, end)
}

 */
/*fun userInput(): Int? {
    println("Enter your guess: ")
    val guess = readln().toIntOrNull()
    return guess
}

fun bodyGame() {
    val randomNumber = Random.nextInt(1, 100)
    while (true) {
        val guess = userInput()
        if(guess == null){
            println("Please input a number")
            continue
        }
        if (guess < 1 || guess > 100){
            println("You can only pick numbers from 1 to 100")
            continue
        }
        if (guess == randomNumber) {
            println("Congrats! You win!!!")
            break
        }
        if (randomNumber > guess) {
            println("More")
        } else {
            println("Less")
        }
    }
}


fun main() {
    bodyGame()
}

 */

//val rules = mapOf(
//    3 to "fizz",
//    5 to "buzz",
//    4 to "haha"
//)

//fun main(){
//    for(i in 1..100) {
//        var wasPrintedRules = false
//        for(entry in rules){
//            if(i % entry.key == 0){
//                print(entry.value + " ")
//                wasPrintedRules = true
//                continue
//            }
//        }
//        if(wasPrintedRules) {
//            println()
//        } else {
//            println(i)
//        }
//    }
//}

