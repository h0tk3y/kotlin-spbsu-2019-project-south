import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

class MessageBase(val connection:Connection) {
    fun add(message: Message) : Long{
        var messageId = -1
        try {
            @Language("MySQL")
            val query = """
                INSERT INTO messages(text, chat_id, user_id,
                 is_edited, is_read, is_sent, is_deleted)
                VALUES (?, ?, ?, ?, ?, ?, ?) 
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS
            )
            preparedStatement.setString(1, message.text)
            preparedStatement.setInt(2, message.chatId.toInt())
            preparedStatement.setInt(3, message.userId.toInt())
            preparedStatement.setBoolean(4, message.isEdited)
            preparedStatement.setBoolean(5, message.isRead)
            preparedStatement.setBoolean(6, message.isSent)
            preparedStatement.setBoolean(7, message.isDeleted)
            preparedStatement.execute()

            val rs = preparedStatement.generatedKeys
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
            preparedStatement.close()
            return messageId.toLong()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun remove(messageId: Long) {
        try {
            @Language("MySQL")
            val query = """
                DELETE FROM messages
                WHERE message_id = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setInt(1, messageId.toInt())
            preparedStatement.execute()
            preparedStatement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun edit(messageId: Long, message: Message) {
        try {
            @Language("MySQL")
            val query = """
                UPDATE messages
                SET
                    text = ?,
                    chat_id = ?, 
                    user_id = ?,
                    is_edited = ?,
                    is_read = ?,
                    is_sent = ?,
                    is_deleted = ?
                WHERE 
                    message_id = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, message.text)
            preparedStatement.setInt(2, message.chatId.toInt())
            preparedStatement.setInt(3, message.userId.toInt())
            preparedStatement.setBoolean(4, message.isEdited)
            preparedStatement.setBoolean(5, message.isRead)
            preparedStatement.setBoolean(6, message.isSent)
            preparedStatement.setBoolean(7, message.isDeleted)
            preparedStatement.setInt(8, messageId.toInt())
            preparedStatement.execute()

            preparedStatement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun get(messageId: Long): Message? {
        try {
            @Language("MySQL")
            val query = """
                SELECT text, chat_id, user_id, is_edited, 
                is_read, is_sent, is_deleted
                FROM messages
                WHERE message_id = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setInt(1, messageId.toInt())
            preparedStatement.execute()
            val rs = preparedStatement.resultSet
            if (rs.next()) {
                val message = Message(
                    rs.getString(1),
                    messageId,
                    rs.getInt(2).toLong(),
                    rs.getInt(3).toLong()
                )
                message.isEdited = rs.getBoolean(4)
                message.isRead = rs.getBoolean(5)
                message.isSent = rs.getBoolean(6)
                message.isDeleted = rs.getBoolean(7)
                return message
            }
            preparedStatement.close()
            return null
        } catch (se: SQLException) {
            throw se
        }
    }
}