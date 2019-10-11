class UserBase: DataBase {
    override var baseSize: Int = 0
    private val users = mutableMapOf<Long, User>()
    fun add(user : User): Int {
        users.plus(Pair(baseSize, user))
        return baseSize++
    }
    fun get(id : Long): User? {
        return users[id]
    }
    fun edit(id : Long, editted_user: User) {
        if (!users.containsKey(id))
            return
        users[id] = editted_user;
    }
    fun remove(id: Long) {
        users.remove(id)
    }
}