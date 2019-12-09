package consoleUI

import client.ServerException

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

fun readNotEmptyLine() : String {
    while (true) {
        val s = readLine()
        if (s == "" || s == null) {
            println("Invalid input. Please, try again.")
            continue
        }
        return s
    }
}

fun readLong() : Long {
    while (true) {
        try {
            return readNotEmptyLine().toLong()
        } catch (e: NumberFormatException) {
            println("Invalid input. Please, try again.")
            continue
        }
    }
}

fun readUserId() : Long {
    while (true) {
        val id = readLong()
        try {
            client.Client.UserDataHandler(id).getName()
            return id
        }
        catch (e : ServerException) {
            println("This user does not exists. Please try again.")
            continue
        }
    }
}

fun readMessageId() : Long {
    while (true) {
        val id = readLong()
        try {
            client.Client.MessageDataHandler(id).getUserId()
            return id
        }
        catch (e : ServerException) {
            println("This message does not exists. Please try again.")
            continue
        }
    }
}

fun printException(e : ServerException) {
    println("${e.cause}")
}
