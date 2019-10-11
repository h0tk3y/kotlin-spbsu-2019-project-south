class UserBase {
    private val users = mutableMapOf<Long, Message>()
    private var baseSize : Int = 0
    fun addUser(user : User): Int {
        users.plus(Pair(baseSize, user))
        return baseSize++
    }
    fun getUser(id : Long): Message? {
        return users[id]
    }
}