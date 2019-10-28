class UserBase(val dataPath: String = ""): DataBase{
    override var baseSize: Long = 0
    private val users = mutableMapOf<Long, User>()

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

    fun add(user : User): Long {
        user.id = baseSize
        users.put(baseSize, user)
        return baseSize++
    }

    fun get(id: Long): User? {
        return users[id]
    }

    fun edit(id : Long, editedUser: User) {
        if (!users.containsKey(id))
            return
        users[id] = editedUser
    }

    fun remove(id: Long) {
        users.remove(id)
    }

}