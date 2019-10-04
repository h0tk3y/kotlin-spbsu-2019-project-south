class UserBase {
    private val users = mutableMapOf<Long, Message>()
    private var baseSize : Int = 0
    fun addUser(user : User) {
        users.plus(Pair(baseSize, user))
        baseSize += 1
    }
    fun getUser(id : Long): Message? {
        return users[id]
    }
}