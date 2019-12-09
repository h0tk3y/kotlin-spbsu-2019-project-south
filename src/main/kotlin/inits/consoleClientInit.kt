import client.Client
import io.ktor.client.HttpClient
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
        Client.LoginDataHandler().registerUser("aca", "aca", "aca", "aca")
    } catch (e : client.ServerException) {
        println(e.message)
    }
    Client.LoginDataHandler().loginUser("aba", "aba")
    println(Client.webClient.token)
    Client.LoginDataHandler().loginUser("aca", "aca")
    println(Client.webClient.token)
    println(555555)
}