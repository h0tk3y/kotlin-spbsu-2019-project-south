package consoleUI

import client.ServerException

class IOException(cause : String) : Exception(cause)

private fun printOptions(options: List<String>) = options.mapIndexed { index, s -> println("> $index -- $s") }

private fun optionNumReader(string: String?, maxRange: Int): Int {
    if (string == null) return -2
    val x: Int? = string.toIntOrNull()
    if (x == null || x < -1 || x >= maxRange) return -2
    return x
}

fun optionsIO(options: List<String>, withReturn : Boolean = false): Int {
    printOptions(options)
    if (!withReturn) println("Print -1 to exit")
    var optionNum = -2
    while (optionNum == -2) {
        optionNum = optionNumReader(readLine(), options.size)
        if (optionNum == -1 && withReturn) optionNum = -2
        if (optionNum == -2) {
            println("Invalid input format, please try again:")
        }
    }
    return optionNum
}

fun readNotEmptyLine() : String {
    while (true) {
        val s = readLine()
        if (s == "" || s == null) continue
        return s
    }
}

fun readLong() : Long {
    try { return readNotEmptyLine().toLong() }
    catch (e: NumberFormatException) { throw IOException("Invalid format") }
}

fun readUserId() : Long {
    val id = readLong()
    try {
        client.Client.UserDataHandler(id).getName()
        return id
    }
    catch (e : ServerException) { throw IOException("Invalid format") }
}

fun readMessageId() : Long {
    val id = readLong()
    try {
        client.Client.MessageDataHandler(id).getUserId()
        return id
    } catch (e: ServerException) {
        throw IOException("Invalid format")
    }
}

fun printException(e : Exception) {
    println(e.message)
}
