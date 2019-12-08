package consoleUI

private fun printOptions(options: List<String>) = options.mapIndexed { index, s -> println("> $index -- $s") }

private fun optionNumReader(string: String?, maxRange: Int): Int {
    if (string == null) {
        return -1
    }
    val x: Int? = string.toIntOrNull()
    if (x == null || x <= -1 || x >= maxRange) {
        return -1
    }
    return x
}

fun optionsIO(options: List<String>): Int {
    printOptions(options)
    var optionNum = -1
    while (optionNum == -1) {
        optionNum = optionNumReader(readLine(), options.size)
        if (optionNum == -1) {
            println("Invalid input format, please try again:")
        }
    }
    return optionNum
}