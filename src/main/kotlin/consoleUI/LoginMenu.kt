package consoleUI

import client.ServerException
import client.Client as client

class LoginMenu {
    fun mainAction() {
        while (true) {
            println("Welcome to SnailMail!")
            println("Please, sign in with your login or sign up:")
            when (optionsIO(listOf("Sign in", "Sign up", "Exit"))) {
                0 -> signInAction()
                1 -> signUpAction()
                2 -> browserExit()
            }
        }
    }


    private fun signInAction() {
        println("Enter your login:")
        val login = readNotEmptyLine()
        println("Enter your password:")
        val password = readNotEmptyLine()
        try {
            client.LoginDataHandler().loginUser(login = login, password = password)
            MainMenu().mainAction()
        }
        catch (e : ServerException) { printException(e) }
    }


    private fun signUpAction() {
        println("Enter your login:")
        val login = readNotEmptyLine()
        println("Enter your password:")
        val password = readNotEmptyLine()
        println("Enter your name:")
        val name = readNotEmptyLine()
        println("Enter your email:")
        val email = readNotEmptyLine()
        try {
            client.LoginDataHandler()
                .registerUser(login = login, password = password, name = name, email = email)
            MainMenu().mainAction()
        }
        catch (e : ServerException) { printException(e) }
    }
}