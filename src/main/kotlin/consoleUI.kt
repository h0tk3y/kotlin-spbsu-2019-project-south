fun getId() :Long {
    return Client.loggedUserId
}

fun getName(id :Long = getId()) : String{
    return Client.UserData(id).getName()
}

fun getLogin(id :Long = getId()) : String{
    return Client.UserData(id).getLogin()
}

fun printOptions(options: List<String>) {
    options.mapIndexed { index, s ->  println("> $index -- $s") }
}

fun optionNumReader(string: String?, maxRange : Int) : Int{
    if (string == null){
        return -1
    }
    val x: Int? = string.toIntOrNull()
    if(x == null || x <= -1 || x >= maxRange){
        return -1
    }
    return x
}

fun optionsIO(options: List<String>) : Int{
    printOptions(options)
    var optionNum = -1
    while (optionNum == -1){
        optionNum = optionNumReader(readLine(), options.size)
        if (optionNum == -1){
            println("Invalid input format, please try again:")
        }
    }
    return optionNum
}

fun browserExit(){

}

fun signIn(){
    println("Sori ne podvezli")
}

fun signUp(){
    println("Enter your login:")
    val login = readLine()
    println("Enter your name:")
    val name = readLine()
    Client.registerUser(login = login!!, name = name!!)
}

class MainMenu{
    fun mainAction(){

        val optionsList = listOf<String>(
            "View my profile",
            "View my contacts",
            "View my chats",
            "View blocked users",
            "Add new contact",
            "Create new chat",
            "Sign out",
            "Exit"
        )

        while (true){
            println("Your are in main menu")
            when(optionsIO(optionsList)){
                0 -> ProfileMenu().mainAction()
                1 -> ContactsMenu().mainAction()
                2 -> ChatsMenu().mainAction()
                3 -> BlockedUsersMenu().mainAction()
                4 -> NewContactMenu().mainAction()
                5 -> ProfileMenu().mainAction()
                6 -> LoginMenu().mainAction()
                7 -> browserExit()
            }
        }
    }
}

class LoginMenu{
    fun mainAction(){
        println("Welcome to SnailMail!")
        println("Please, sign in with your login or sign up:")
        when(optionsIO(listOf("Sign in", "Sign up", "Exit"))){
            0 -> signIn()
            1 -> signUp()
            2 -> browserExit()
        }
    }
}

class ProfileMenu{
    fun mainAction(){
        println("Your are in your profile menu")
        println("Your ID: ${getId()}")
        println("Your login: ${getLogin()}")
        println("Your name: ${getName()}")

        when(optionsIO(listOf("Change name", "Return"))){
            0 -> {
                println("Enter your name:")
                val newName = readLine()
                Client.UserData().changeName(newName!!)
            }
            1 -> return
        }
    }
}


class ContactsMenu{
    private fun contacts() = Client.UserData().getContacts()

    fun mainAction(){
        println("Your are in your contacts menu")

        val options = listOf<String>(
            "Show your contacts",
            "Add new contact",
            "Remove contact",
            "Change contact name",
            "Return"
        )
        while (true){
            println("Your are in your contacts menu")
            when(optionsIO(options)){
                0 -> showContactsAction()
                1 -> addNewContactAction()
                2 -> removeContactAction()
                3 -> changeContactNameAction()
                4 -> return
            }
        }

    }

    private fun contactFormat(contact : Map.Entry<Long, String>) : String{
        return "ID: ${contact.key}, login: ${getLogin(contact.key)}, name: ${contact.value}"
    }

    private fun showContactsAction(){
        contacts().map {
            println(contactFormat(it))
        }
    }

    private fun addNewContactAction(){
        TODO() // login db
    }

    private fun removeContactAction(){
        var contactIdbyNum : MutableMap<Int, Long> = mutableMapOf()
        var i = 0
        for (contact in contacts()){
            contactIdbyNum.put(i, contact.key)
        }
        // TODO !!!
        println("Select contact to remove:")
        val num = optionsIO(contacts().map { contactFormat(it) })
        Client.UserData().deleteContact(contactIdbyNum[num]!!)
    }

    private fun changeContactNameAction(){

    }
}

class ChatsMenu{
    fun mainAction(){
        println("Your are in your chats menu")
    }
}

class BlockedUsersMenu{
    fun mainAction(){
        println("Your are in your blocked users menu")
    }
}

class NewContactMenu{
    fun mainAction(){
        println("Your are in adding new contact menu")
    }
}

class NewChatMenu{
    fun mainAction(){
        println("Your are in creating new chat menu")
    }
}



fun main() {
    LoginMenu().mainAction()
    MainMenu().mainAction()
}