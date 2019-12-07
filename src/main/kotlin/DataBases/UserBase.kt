import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement


/***
 * You CANT'T call function edit() in order to change users contacts
 * You MUST use editContact instead
 */

class UserBase(val connection: Connection) {

    fun add(user: User): Long {
        var userId: Int = -1;
        try {
            @Language("MySQL")
            val queryInsert = """
                INSERT INTO users(login, name, email)
                VALUES (?, ?, ?);
            """
            // '${user.login}', '${user.name}', '${user.email}'
            val prStatementUser = connection.prepareStatement(
                queryInsert,
                Statement.RETURN_GENERATED_KEYS
            )
            prStatementUser.setString(1, user.login)
            prStatementUser.setString(2, user.name)
            prStatementUser.setString(3, user.email)
            prStatementUser.executeUpdate()
            val rs = prStatementUser.generatedKeys
            if (rs.next()) {
                userId = rs.getInt(1);
            }
            prStatementUser.close()

            for ((otherId, otherName) in user.contacts) {
                @Language("MySQL")
                val queryContact = """
                   INSERT INTO related_users(user_id, other_user_id, is_blocked, name)
                   VALUES (?, ?, ?, ?);
                """
                // $userId, $otherId, FALSE, '$otherName'
                val prStatementContact = connection.prepareStatement(
                    queryContact,
                    Statement.RETURN_GENERATED_KEYS
                )
                prStatementContact.setInt(1, userId)
                prStatementContact.setInt(2, otherId.toInt())
                prStatementContact.setBoolean(3, false)
                prStatementContact.setString(4, otherName)
                prStatementContact.executeUpdate()
                prStatementContact.close()
            }

            for (otherId in user.blockedUsers) {
                @Language("MySQL")
                val queryBlocked = """
                   INSERT INTO related_users(user_id, other_user_id, is_blocked, name)
                   VALUES (?, ?, ?, ?);
                """
                // $$userId, $otherId, TRUE, ''
                val prStatementBlocked = connection.prepareStatement(
                    queryBlocked,
                    Statement.RETURN_GENERATED_KEYS
                )
                prStatementBlocked.setInt(1, userId)
                prStatementBlocked.setInt(2, otherId.toInt())
                prStatementBlocked.setBoolean(3, true)
                prStatementBlocked.setString(4, "")
                prStatementBlocked.executeUpdate()
            }

            return userId.toLong()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun get(userId: Long): User? {
        try {
            @Language("MySQL")
            val queryUser = """
                SELECT * FROM users
                WHERE user_id = ?
            """
            val prStatementUser = connection.prepareStatement(
                queryUser
            )
            prStatementUser.setInt(1, userId.toInt())
            prStatementUser.execute()
            val rs = prStatementUser.resultSet
            if (rs.next()) {
                val user = User(rs.getInt("user_id").toLong(),
                    rs.getString("login"),
                    rs.getString("name"))
                user.email = rs.getString("email")
                return user
            }
            prStatementUser.close()
        } catch (se: SQLException) {
            throw se
        }
        return null
    }

    fun remove(userId: Long) {
        try {
            @Language("MySQL")
            val queryUser = """
                DELETE FROM users
                WHERE user_id = ?
            """
            val prStatementUser = connection.prepareStatement(
                queryUser
            )
            prStatementUser.setInt(1, userId.toInt())
            prStatementUser.execute()
            prStatementUser.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    // TODO: remove userId
    fun edit(userId: Long, user: User) {
        try {
            @Language("MySQL")
            val queryInsert = """
                UPDATE users
                SET
                    name = ?,
                    email = ?
                WHERE 
                    user_id = ?
            """
            val prStatementUser = connection.prepareStatement(
                queryInsert
            )
            prStatementUser.setString(1, user.name)
            prStatementUser.setString(2, user.email)
            prStatementUser.setInt(3, userId.toInt())
            prStatementUser.execute()
            prStatementUser.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getChats(userId: Long): MutableMap<Long, String> {
        val chats = mutableMapOf<Long, String>()
        try {
            @Language("MySQL")
            val queryChats = """
                SELECT c.chat_id, name FROM chat_members
                LEFT JOIN chats c on chat_members.chat_id = c.chat_id
                WHERE user_id = ?;
            """
            val preparedStatement = connection.prepareStatement(
                queryChats
            )
            preparedStatement.setInt(1, userId.toInt())
            preparedStatement.execute()
            val rs = preparedStatement.resultSet
            while (rs.next()) {
                val chatId = rs.getInt(1)
                val chatName = rs.getString(2)
                chats.put(chatId.toLong(), chatName)
            }
            @Language("MySQL")
            val queryOwners = """
                SELECT chat_id, name FROM chats
                WHERE owner_id = ?;
            """.trimIndent()
            val preparedStatementOwner = connection.prepareStatement(
                queryOwners
            )
            preparedStatementOwner.setInt(1, userId.toInt())
            preparedStatementOwner.execute()
            val rsOwner = preparedStatementOwner.resultSet

            while (rsOwner.next()) {
                val chatId = rsOwner.getInt(1)
                val chatName = rsOwner.getString(2)
                chats.put(chatId.toLong(), chatName)
            }
            preparedStatementOwner.close()
            preparedStatement.close()
            return chats
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getContacts(userId: Long): MutableMap<Long, String> {
        val contacts = mutableMapOf<Long, String>()
        try {
            @Language("MySQL")
            val queryChats = """
                SELECT other_user_id, name FROM related_users
                WHERE user_id = ?
                AND is_blocked = FALSE
            """
            val preparedStatement = connection.prepareStatement(
                queryChats
            )
            preparedStatement.setInt(1, userId.toInt())
            preparedStatement.execute()
            val rs = preparedStatement.resultSet
            while (rs.next()) {
                val contactId = rs.getInt(1)
                val contactName = rs.getString(2)
                contacts.put(contactId.toLong(), contactName)
            }
            preparedStatement.close()
            return contacts
        } catch (se: SQLException) {
            throw se
        }
    }

    private fun queryRelatedUser(query: String, userId: Long, contactId: Long, newName: String, isBlocked: Boolean) {
        try {
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, newName)
            preparedStatement.setInt(2, userId.toInt())
            preparedStatement.setInt(3, contactId.toInt())
            preparedStatement.setBoolean(4, isBlocked)
            preparedStatement.execute()
            preparedStatement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    private fun editRelatedUser(userId: Long, contactId: Long, newName: String) {
        @Language("MySQL")
        val query = """
            UPDATE related_users
            SET 
                name = ?
            WHERE 
                user_id = ?
            AND 
                other_user_id = ?
            AND 
                is_blocked = ?
        """.trimIndent()
        try {
            queryRelatedUser(query, userId, contactId, newName, false)
        } catch (se: SQLException) {
            throw se
        }
    }

    private fun addRelatedUser(userId: Long, contactId: Long, name: String, isBlocked: Boolean) {
        @Language("MySQL")
        val query = """
            INSERT INTO related_users(name, user_id, other_user_id, is_blocked)
            VALUES (?, ?, ?, ?);
        """.trimIndent()
        try {
            queryRelatedUser(query, userId, contactId, name, isBlocked)
        } catch (se: SQLException) {
            throw se
        }
    }

    private fun removeRelatedUser(userId: Long, contactId: Long, name: String, isBlocked: Boolean) {
        @Language("MySQL")
        val query = """
            DELETE FROM related_users
            WHERE 
                name = ?
            AND 
                user_id = ?
            AND 
                other_user_id = ?
            AND 
                is_blocked = ?
        """.trimIndent()
        try {
            queryRelatedUser(query, userId, contactId, name, isBlocked)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun editContact(userId: Long, contactId: Long, newName: String) =
        editRelatedUser(userId, contactId, newName)

    fun addContact(userId: Long, contactId: Long, name: String) =
        addRelatedUser(userId, contactId, name, false)

    // TODO: REMOVE NAME FROM THIS FUNCTION
    fun removeContact(userId: Long, contactId: Long, name: String) =
        removeRelatedUser(userId, contactId, name, false)

    fun blockUser(userId: Long, blockedId: Long) =
        addRelatedUser(userId, blockedId, "", true)

    fun unblockUser(userId: Long, blockedId: Long) =
        removeRelatedUser(userId, blockedId, "", true)

    fun isBlocked(userId: Long, otherId: Long) : Boolean {
        try {
            @Language("MySQL")
            val query = """
                SELECT * FROM related_users
                WHERE is_blocked = TRUE
                AND  user_id = ?
                AND other_user_id = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(
                query
            )
            preparedStatement.setInt(1, userId.toInt())
            preparedStatement.setInt(2, otherId.toInt())
            preparedStatement.execute()
            val rs = preparedStatement.resultSet
            return rs.next()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getBlocked(userId: Long): MutableSet<Long> {
        val blocks = mutableSetOf<Long>()
        try {
            @Language("MySQL")
            val queryChats = """
                SELECT other_user_id FROM related_users
                WHERE user_id = ?
                AND is_blocked = TRUE
            """
            val preparedStatement = connection.prepareStatement(
                queryChats
            )
            preparedStatement.setInt(1, userId.toInt())
            preparedStatement.execute()
            val rs = preparedStatement.resultSet
            while (rs.next()) {
                val blockedId = rs.getInt(1)
                blocks.add(blockedId.toLong())
            }
            preparedStatement.close()
            return blocks
        } catch (se: SQLException) {
            throw se
        }
    }

}