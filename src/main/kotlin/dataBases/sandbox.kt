package dataBases

import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

fun main() {
    val connectionProps = Properties()
    connectionProps.put("user", "root")
    connectionProps.put("password", "root")
    try {
        val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/localhost",
            connectionProps)
        val stmt = conn.createStatement();

        val query: String = "SELECT * FROM users;"

        var rs : ResultSet? = null
        if (stmt.execute(query)) {
            rs = stmt.resultSet;
        }
        while (rs!!.next()) {
            println(rs.getString("name"))
        }
    } catch (ex: SQLException) {
        // handle any errors
        ex.printStackTrace()
    } catch (ex: Exception) {
        // handle any errors
        ex.printStackTrace()
    }
}
