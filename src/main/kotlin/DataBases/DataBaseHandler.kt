package DataBases

import org.intellij.lang.annotations.Language
import java.sql.*
import User
import Chat
import Message

class DataBaseHandler {
    // TODO: Add Logging
    // TODO: Add exception handling
    // TODO: Add Commits, transations
    // TODO: SQL INJECTIONS
    private val JDBC_DRIVER = "com.mysql.jdbc.Driver"
    private val DB_URL = "jdbc:mysql://localhost:3306/"

    private val USER = "root"
    private val PASS = "root"

    var connection: Connection? = null

    private val DB_NAME = "snailmail"

    private fun convertToBOOL(flag: Boolean) : String = if (flag) {"TRUE"} else {"FALSE"}

    private fun createDB() {
        try {
            val statement = connection!!.createStatement()
            val query = "CREATE DATABASE IF NOT EXISTS  ${DB_NAME}"
            statement.executeUpdate(query)
            statement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    private fun createAllTables() {
        try {
            val statement = connection!!.createStatement()
            @Language("MySQL")
            val queryUsers = """
                CREATE TABLE IF NOT EXISTS users(
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    login varchar(50),
                    name varchar(50),
                    email varchar(50)
                );"""

            statement.execute(queryUsers)

            @Language("MySQL")
            val queryRelated = """
                CREATE TABLE IF NOT EXISTS related_users(
                    related_user_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id  INT,
                    other_user_id INT,
                    is_blocked BOOL,
                    name varchar(50),
                    FOREIGN KEY (user_id)
                        REFERENCES users(user_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (other_user_id)
                        REFERENCES users(user_id)
                        ON DELETE CASCADE,
                    INDEX(user_id),
                    INDEX(is_blocked)
                );"""
            statement.execute(queryRelated)

            @Language("MySQL")
            val queryChats = """
                CREATE TABLE IF NOT EXISTS chats(
                    chat_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR (50), 
                    is_single BOOL
                );"""
            statement.execute(queryChats)


            @Language("MySQL")
            val queryMembers = """
                CREATE TABLE IF NOT EXISTS chat_members(
                    member_id INT AUTO_INCREMENT PRIMARY KEY,
                    is_owner BOOL,
                    is_admin BOOL, 
                    chat_id INT, 
                    user_id INT, 
                    FOREIGN KEY (user_id)
                        REFERENCES users(user_id)
                         ON DELETE CASCADE,
                    FOREIGN KEY (chat_id)
                        REFERENCES chats(chat_id)
                         ON DELETE CASCADE,
                    INDEX(chat_id), 
                    INDEX(user_id),
                    INDEX(is_owner), 
                    INDEX(is_admin)
                );"""
            statement.execute(queryMembers)

            @Language("MySQL")
            val queryMessages = """
                CREATE TABLE IF NOT EXISTS messages(
                    message_id INT AUTO_INCREMENT PRIMARY KEY, 
                    text VARCHAR(250), 
                    chat_id INT, 
                    user_id INT, 
                    is_edited BOOL, 
                    is_read BOOL, 
                    is_sent BOOL, 
                    is_deleted BOOL,
                    FOREIGN KEY (chat_id)
                        REFERENCES chats(chat_id)
                         ON DELETE CASCADE,
                    FOREIGN KEY (user_id)
                        REFERENCES users(user_id)
                         ON DELETE CASCADE,
                    INDEX(user_id), 
                    INDEX(chat_id)
                );
            """
            statement.execute(queryMessages)

            statement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun initAll() {
        try {
            // Creating DB
            connection = DriverManager.getConnection(DB_URL, USER, PASS)
            createDB()

            // Creating Tables
            connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS)
            createAllTables()
        } catch (se: SQLException) {
            se.printStackTrace()
            try {
                connection?.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            }
        }
    }

    fun closeAll() {
        try {
            connection?.close()
        } catch (se: SQLException) {
            se.printStackTrace()
        }
    }

    fun addChat(chat: Chat) : Int{
        var chatId: Int = -1
        try {
            @Language("MySQL")
            val queryChat = """
               INSERT INTO chats(name, is_single)
               VALUES (?, ?);
            """
            // '${chat.name}', ${convertToBOOL(chat.isSingle)}
            val prStatementChat = connection!!.prepareStatement(
                queryChat,
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
                // FALSE, FALSE, $chatId, $otherId
                val prStatementMember = connection!!.prepareStatement(
                    queryMember
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
                val queryOwner = """
                   INSERT INTO chat_members(is_owner, is_admin, chat_id, user_id) 
                   VALUES (?, ?, ?, ?);
                """
                // TRUE, FALSE, $chatId, $otherId
                val prStatementOwner = connection!!.prepareStatement(
                    queryOwner
                )
                prStatementOwner.setBoolean(1, true)
                prStatementOwner.setBoolean(2, false)
                prStatementOwner.setInt(3, chatId)
                prStatementOwner.setInt(4, otherId.toInt())
                prStatementOwner.executeUpdate()
                prStatementOwner.close()
            }

            return chatId
        } catch (se: SQLException) {
            throw se
        }
    }

    fun addMessage(message: Message) : Int {
        // TODO: CHECK FOREIGN KEYS ON VALID
        var messageId : Int = -1
        try {
            @Language("MySQL")
            val queryMessage = """
               INSERT INTO messages(text, chat_id, user_id, is_edited, is_read, is_sent, is_deleted)
               VALUES (?, ?, ?, ?, ?, ?, ?); 
            """
            /*
            '${message.text}', ${message.chatId}, ${message.userId},
                       ${convertToBOOL(message.isEdited)},
                       ${convertToBOOL(message.isRead)},
                       ${convertToBOOL(message.isSent)},
                       ${convertToBOOL(message.isDeleted)}
             */
            val prStatementMessage = connection!!.prepareStatement(
                queryMessage,
                Statement.RETURN_GENERATED_KEYS
            )
            prStatementMessage.setString(1, message.text)
            prStatementMessage.setInt(2, message.chatId.toInt())
            prStatementMessage.setInt(3, message.userId.toInt())
            prStatementMessage.setBoolean(4, message.isEdited)
            prStatementMessage.setBoolean(5, message.isRead)
            prStatementMessage.setBoolean(6, message.isSent)
            prStatementMessage.setBoolean(7, message.isDeleted)
            prStatementMessage.executeUpdate()
            val rs = prStatementMessage.generatedKeys
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
            prStatementMessage.close()
            return messageId
        } catch (se: SQLException) {
            throw se
        }
    }

}

fun main() {
    val dbH = DataBaseHandler()
    dbH.initAll()
    /*val user = User(-1, "sp", "Pavlov Ivan")
    user.email = "pavlov200912@mail.ru"
    user.blockedUsers.add(2)
    user.contacts.put(2, "Pashtet Pavlov")
    dbH.addUser(user)
    val chat = Chat(-1, false, "My Chat", setOf<Long>(3, 2) as MutableSet<Long>)
    dbH.addChat(chat)
    val message = Message("Hello world of SQL!", -1, 1, 3)
    message.isSent = true
    message.isRead = false
    message.isDeleted = false
    message.isEdited = false
    dbH.addMessage(message)
    dbH.closeAll()*/
}