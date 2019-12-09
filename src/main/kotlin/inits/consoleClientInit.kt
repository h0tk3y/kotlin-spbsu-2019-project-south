import client.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch { Client.webClient.run() }
    try {
        Client.LoginDataHandler().registerUser("aba", "aba", "aba", "aba")
    } catch (e : client.ServerException) {
        println(e.message)
    }
    try {
        Client.LoginDataHandler().registerUser("ada", "ada", "ada", "ada")
    } catch (e : client.ServerException) {
        println(e.message)
    }
    println(555555)
}