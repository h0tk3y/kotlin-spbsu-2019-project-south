import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch {
        Client.webClient.run()
    }
    consoleUi.LoginMenu().mainAction()
    consoleUi.MainMenu().mainAction()
}