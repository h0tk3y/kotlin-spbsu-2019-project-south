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


}