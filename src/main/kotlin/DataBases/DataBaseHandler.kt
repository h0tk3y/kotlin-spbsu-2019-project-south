package DataBases

import org.intellij.lang.annotations.Language
import java.sql.*

class DataBaseHandler {
    // TODO: Add Logging
    // TODO: Add exception handling
    private val JDBC_DRIVER = "com.mysql.jdbc.Driver"
    private val DB_URL = "jdbc:mysql://localhost:3306/"

    private val USER = "root"
    private val PASS = "root"

    private var connection: Connection? = null
    private var statement: Statement? = null

    private val DB_NAME = "snailmail"

    private fun createDB() {
        try {
            val query = "CREATE DATABASE IF NOT EXISTS  ${DB_NAME}"
            statement!!.executeUpdate(query)
        } catch (se: SQLException) {
            throw se
        }
    }

    private fun createAllTables() {
        try {
            @Language("MySQL")
            val queryUsers = """
                CREATE TABLE IF NOT EXISTS users(
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    login varchar(50),
                    name varchar(50),
                    email varchar(50)
                );"""
            statement!!.execute(queryUsers)

            @Language("MySQL")
            val queryRelated = """
                CREATE TABLE IF NOT EXISTS related_users(
                    related_user_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id  INT,
                    other_user_id INT,
                    is_blocked BOOL,
                    name varchar(50),
                    FOREIGN KEY (user_id)
                        REFERENCES users(user_id),
                    FOREIGN KEY (other_user_id)
                        REFERENCES users(user_id),
                    INDEX(user_id),
                    INDEX(is_blocked)
                );"""
            statement!!.execute(queryRelated)

            @Language("MySQL")
            val queryChats = """
                CREATE TABLE IF NOT EXISTS chats(
                    chat_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR (50), 
                    is_single BOOL
                );"""
            statement!!.execute(queryChats)


            @Language("MySQL")
            val queryMembers = """
                CREATE TABLE IF NOT EXISTS chat_members(
                    member_id INT AUTO_INCREMENT PRIMARY KEY,
                    is_owner BOOL,
                    is_admin BOOL, 
                    chat_id INT, 
                    user_id INT, 
                    FOREIGN KEY (user_id)
                        REFERENCES users(user_id),
                    FOREIGN KEY (chat_id)
                        REFERENCES chats(chat_id),
                    INDEX(chat_id), 
                    INDEX(user_id),
                    INDEX(is_owner), 
                    INDEX(is_admin)
                );"""
            statement!!.execute(queryMembers)

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
                        REFERENCES chats(chat_id),
                    FOREIGN KEY (user_id)
                        REFERENCES users(user_id),
                    INDEX(user_id), 
                    INDEX(chat_id)
                );
            """
            statement!!.execute(queryMessages)
        } catch (se: SQLException) {
            throw se
        }
    }
    fun initAll() {
        try {
            // Creating DB
            connection = DriverManager.getConnection(DB_URL, USER, PASS)
            statement = connection!!.createStatement()
            createDB()

            // Creating Tables
            connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS)
            statement = connection!!.createStatement()
            createAllTables()
        } catch (se: SQLException) {
            se.printStackTrace()
        } finally {
            try {
                statement?.close()
            } catch (se2: SQLException) {
            }
            try {
                connection?.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            }
        }
    }
}

fun main() {
    val dbH = DataBaseHandler()
    dbH.initAll()
}