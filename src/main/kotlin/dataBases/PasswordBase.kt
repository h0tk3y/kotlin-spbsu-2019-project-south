package dataBases

import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.SQLException

class PasswordBase(val connection: Connection){
    // IF LOGIN EXIST THROW EXCEPTION
    fun add(login: String, password: String) {
        try {
            @Language("MySQL")
            val query = """
                INSERT INTO password(login, password)
                VALUES (?, ?) 
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(
                query
            )
            preparedStatement.setString(1, login)
            preparedStatement.setString(2, password)
            preparedStatement.execute()
            preparedStatement.close()
        } catch (se: SQLException) {
            throw se
        }
    }

    fun checkPassword(login: String, password: String): Boolean {
        try {
            @Language("MySQL")
            val query = """
                SELECT login, password
                FROM password
                WHERE login = ?
                AND password = ?
            """.trimIndent()
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, login)
            preparedStatement.setString(2, password)
            preparedStatement.execute()
            val rs = preparedStatement.resultSet
            val check =  rs.next()
            preparedStatement.close()
            return check
        } catch (se: SQLException) {
            throw se
        }
    }

}