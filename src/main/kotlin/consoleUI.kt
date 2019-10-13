val client = Client()


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

fun signIn(){
    println("Sori ne podvezli")
}

fun signUp(){
    println("Enter your login:")
    val login = readLine()
    println("Enter your name:")
    val name = readLine()
    client.registerUser(login = login!!, name = name!!)
}

fun init() {
    println("Welcome to SnailMail!")
    println("Please, sign in with your login or sign up:")
    when(optionsIO(listOf("Sign in", "Sign up"))){
        0 -> signIn()
        1 -> signUp()
    }
}

class MainMenu(){

}


fun main() {
    init()
1
}