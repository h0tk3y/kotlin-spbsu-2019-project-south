package DataBases

import org.intellij.lang.annotations.Language
import java.sql.*
import User
import Chat
import Message
import UserBase

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
                    is_single BOOL,
                    owner_id INT
                );"""
            statement.execute(queryChats)


            @Language("MySQL")
            val queryMembers = """
                CREATE TABLE IF NOT EXISTS chat_members(
                    member_id INT AUTO_INCREMENT PRIMARY KEY,
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
            connection = DriverManager.getConnection("$DB_URL?autoReconnect=true&useSSL=false", USER, PASS)
            createDB()

            // Creating Tables
            connection = DriverManager.getConnection("$DB_URL$DB_NAME?autoReconnect=true&useSSL=false", USER, PASS)
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


}

fun main() {
    val dbH = DataBaseHandler()
    dbH.initAll()
    dbH.closeAll()
}