import client.Client
import consoleUI.LoginMenu
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch { Client.webClient.run() }
    LoginMenu().mainAction()
}