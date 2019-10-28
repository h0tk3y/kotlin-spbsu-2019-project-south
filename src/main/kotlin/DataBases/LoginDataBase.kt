class LoginDataBase : DataBase {
    override var baseSize: Long = 0

    private val loginData = mutableMapOf<String, LoginData>() // login/LoginDATA

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

    fun add(newLoginData: LoginData) {
        loginData.put(newLoginData.login, newLoginData)
        baseSize++
    }

    fun get(login :String): LoginData? {
        return loginData[login]
    }

    fun edit(login :String, editedLoginData: LoginData) {
        if (!loginData.containsKey(login))
            return
        loginData[login] = editedLoginData
    }

    fun remove(login :String) {
        loginData.remove(login)
    }
}