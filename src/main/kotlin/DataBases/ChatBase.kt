import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

class ChatBase(val connection: Connection) {
    /***
     * ChatBase invariants:
     * Owner is not Admin
     * Admin is not Owner
     * Owners can be added only by addChat function
     * If you add admin, DB add it only as admin, not as member
     */

    /***
     * Ignore Chat messages field
     * To add messages use MessageBase
     */
    fun add(chat: Chat) : Long{
        var chatId: Int = -1;
        try {
            @Language("MySQL")
            val queryInsert = """
                INSERT INTO chats(name, is_single)
                VALUES (?, ?);
            """
            // '${user.login}', '${user.name}', '${user.email}'
            val prStatementChat = connection.prepareStatement(
                queryInsert,
                Statement.RETURN_GENERATED_KEYS
            )
            prStatementChat.setString(1, chat.name)
            prStatementChat.setBoolean(2, chat.isSingle)
            prStatementChat.executeUpdate()
            val rs = prStatementChat.generatedKeys
            if (rs.next()) {
                chatId = rs.getInt(1);
            }
            prStatementChat.close()

            for (otherId in chat.members) {
                @Language("MySQL")
                val queryMember = """
                   INSERT INTO chat_members(is_owner, is_admin, chat_id, user_id)
                   VALUES (?, ?, ?, ?);
                """
                val prStatementMember = connection.prepareStatement(
                    queryMember,
                    Statement.RETURN_GENERATED_KEYS
                )
                prStatementMember.setBoolean(1, false)
                prStatementMember.setBoolean(2, false)
                prStatementMember.setInt(3, chatId)
                prStatementMember.setInt(4, otherId.toInt())
                prStatementMember.executeUpdate()
                prStatementMember.close()
            }


            for (otherId in chat.owners) {
                @Language("MySQL")
                val queryMember = """
                   INSERT INTO chat_members(is_owner, is_admin, chat_id, user_id)
                   VALUES (?, ?, ?, ?);
                """
                val prStatementMember = connection.prepareStatement(
                    queryMember,
                    Statement.RETURN_GENERATED_KEYS
                )
                prStatementMember.setBoolean(1, true)
                prStatementMember.setBoolean(2, false)
                prStatementMember.setInt(3, chatId)
                prStatementMember.setInt(4, otherId.toInt())
                prStatementMember.executeUpdate()
                prStatementMember.close()
            }
            return chatId.toLong()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun remove(chatId: Long) {
        try {
            @Language("MySQL")
            val queryUser = """
                DELETE FROM chats
                WHERE chat_id = ?
            """
            val prStatementChat = connection.prepareStatement(
                queryUser
            )
            prStatementChat.setInt(1, chatId.toInt())
            prStatementChat.execute()
            prStatementChat.close()
        } catch (se: SQLException) {
            throw se
        }
    }


    /***
     * Ignore members/owners
     * If you want to edit them, use
     * addMember, removeMember and so on
     */
    // TODO: Remove chatId
    fun edit(chatId: Long, chat: Chat) {
        try {
            @Language("MySQL")
            val queryUpdate = """
                UPDATE chats
                SET
                    name = ?,
                    is_single = ?
                WHERE 
                    chat_id = ?
            """
            val prStatementChat = connection.prepareStatement(
                queryUpdate
            )
            prStatementChat.setString(1, chat.name)
            prStatementChat.setBoolean(2, chat.isSingle)
            prStatementChat.setInt(3, chatId.toInt())
            prStatementChat.execute()
            prStatementChat.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    /***
     * @return Chat(id, isSingle, name, emptySet())
     * If you want to get members/owners
     * Use getMembers/getOwners instead
     */
    fun get(chatId: Long): Chat? {
        try {
            @Language("MySQL")
            val queryChat = """
                SELECT * FROM chats
                WHERE chat_id = ?
            """
            val prStatementChat = connection.prepareStatement(
                queryChat
            )
            prStatementChat.setInt(1, chatId.toInt())
            prStatementChat.execute()
            val rs = prStatementChat.resultSet
            if (rs.next()) {
                val chat = Chat(rs.getInt("chat_id").toLong(),
                    rs.getBoolean("is_single"),
                    rs.getString("name"))
                return chat
            }
            prStatementChat.close()
        } catch (se: SQLException) {
            throw se
        }
        return null
    }

    // TODO: UNTESTED
    fun getMessages(chatId: Long): MutableSet<Message> {
        try {
            @Language("MySQL")
            val query = """
                SELECT text, message_id, c.chat_id, user_id,
                 is_edited, is_read, is_sent, is_deleted FROM messages
                LEFT JOIN chats c on messages.chat_id = c.chat_id
                WHERE c.chat_id = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(
                query
            )
            preparedStatement.setInt(1, chatId.toInt())
            preparedStatement.execute()
            val rs = preparedStatement.resultSet

            val messages = mutableSetOf<Message>()
            while (rs.next()) {
                val message = Message(
                    rs.getString(1),
                    rs.getInt(2).toLong(),
                    rs.getInt(3).toLong(),
                    rs.getInt(4).toLong()
                )
                message.isEdited = rs.getBoolean(5)
                message.isRead = rs.getBoolean(6)
                message.isSent = rs.getBoolean(7)
                message.isDeleted = rs.getBoolean(8)
                messages.add(message)
            }
            return messages
        } catch (se: SQLException) {
            throw se
        }
    }

    private fun getChatUsers(chatId: Long, isOwner: Boolean, isAdmin: Boolean) : MutableSet<Long>{
        try {
            @Language("MySQL")
            val query = """
                SELECT user_id FROM chat_members
                WHERE chat_id = ?
                AND is_admin = ?
                AND is_owner = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(
                query
            )
            preparedStatement.setInt(1, chatId.toInt())
            preparedStatement.setBoolean(2, isAdmin)
            preparedStatement.setBoolean(3, isOwner)
            preparedStatement.execute()
            val rs = preparedStatement.resultSet

            val users = mutableSetOf<Long>()
            while (rs.next()) {
                users.add(rs.getInt(1).toLong())
            }
            return users
        } catch (se: SQLException) {
            throw se
        }
    }

    /***
     * Get all members: Admins, Owners, Members
     */
    fun getMembers(chatId: Long) =
        getChatUsers(chatId, isAdmin = false, isOwner = false).union(
            getAdmins(chatId)
        ).toMutableList()

    fun getAdmins(chatId: Long) =
        getChatUsers(chatId, false, true).union(
            getChatUsers(chatId, true, false)
        ).toMutableSet()

    // TODO: ADD OWNERS IN CHAT ADD
    private fun addChatUser(chatId: Long, userId: Long, isOwner: Boolean, isAdmin: Boolean) {
        try {
            @Language("MySQL")
            val query = """
                INSERT INTO chat_members(is_owner, is_admin, chat_id, user_id) 
                VALUES (?, ?, ?, ?)
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(
                query
            )
            preparedStatement.setBoolean(1, isOwner)
            preparedStatement.setBoolean(2, isAdmin)
            preparedStatement.setInt(3, chatId.toInt())
            preparedStatement.setInt(4, userId.toInt())
            preparedStatement.execute()
            preparedStatement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun addAdmin(chatId: Long, userId: Long) =
        addChatUser(chatId, userId, isOwner = false, isAdmin = true)

    fun addMember(chatId: Long, userId: Long) =
        addChatUser(chatId, userId, isOwner = false, isAdmin = false)


    private fun deleteChatUser(chatId: Long, userId: Long, isOwner: Boolean, isAdmin: Boolean) {
        try {
            @Language("MySQL")
            val query = """
                DELETE FROM chat_members
                WHERE is_owner = ?
                AND is_admin = ?
                AND chat_id = ?
                AND user_id = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(
                query
            )
            preparedStatement.setBoolean(1, isOwner)
            preparedStatement.setBoolean(2, isAdmin)
            preparedStatement.setInt(3, chatId.toInt())
            preparedStatement.setInt(4, userId.toInt())
            preparedStatement.execute()
            preparedStatement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun removeAdmin(chatId: Long, userId: Long) =
        deleteChatUser(chatId, userId, isOwner = false, isAdmin = true)

    /***
     * Remove user from chat as admin and as member
     */
    fun removeMember(chatId: Long, userId: Long) {
        deleteChatUser(chatId, userId, isAdmin = false, isOwner = false)
        deleteChatUser(chatId, userId, isAdmin = true, isOwner = false)
    }

}