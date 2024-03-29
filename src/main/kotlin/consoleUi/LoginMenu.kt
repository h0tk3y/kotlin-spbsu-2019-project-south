package consoleUi

class LoginMenu {
    fun mainAction() {
        println("Welcome to SnailMail!")
        println("Please, sign in with your login or sign up:")
        when (optionsIO(listOf("Sign in", "Sign up", "Exit"))) {
            0 -> signInAction()
            1 -> signUpAction()
            2 -> browserExit()
        }
    }

    private fun signInAction() {
        TODO()
    }

    private fun signUpAction() {
        println("Enter your login:")
        val login = readLine()
        println("Enter your name:")
        val name = readLine()
        println("Enter your email:")
        val email = readLine()
        Client.registerUser(login = login!!, name = name!!, email = email!!)
    }
}