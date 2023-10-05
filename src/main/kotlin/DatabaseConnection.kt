import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.impl.DSL
import org.jooq.SQLDialect
import java.sql.Connection
import java.sql.DriverManager


object DatabaseConnection {

    private var url: String = "jdbc:mariadb://localhost:3306"
    private var connection: Connection? = null
    private var ctx: DSLContext? = null

    fun connect(username: String, password: String, hostUrl: String) {
        connection = DriverManager.getConnection(hostUrl, username, password)
        ctx = DSL.using(connection, SQLDialect.MARIADB)
    }

    fun disconnect() {
        connection?.close()
        ctx = null
        connection = null
    }

    fun databases(): Result<Record>? {
        return ctx?.fetch("SHOW DATABASES")
    }

    fun choose(name: String) {
        ctx?.execute("USE $name")
    }

    fun execute(query: String) : Result<Record>? {
        return ctx?.fetch(query)
    }
}
