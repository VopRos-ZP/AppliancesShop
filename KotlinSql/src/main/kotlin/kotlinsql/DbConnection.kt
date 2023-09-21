package kotlinsql

import kotlinsql.annotations.PrimaryKey
import java.lang.RuntimeException
import java.sql.Connection
import java.sql.DriverManager
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.primaryConstructor

class DbConnection(
    private val database: String,
    private val user: String,
    private val password: String
) {

    val connection: Connection
        get() = try {
            Class.forName("org.postgresql.Driver")
            DriverManager.getConnection("jdbc:postgresql://localhost:5432/$database", user, password)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException()
        }

    inline fun <reified T : Entity> runScript(type: KClass<T>, sql: String): T {
        val fields = type.declaredMembers
            .filter { it.annotations.contains(PrimaryKey()) }
            .map { it.name }
            .toList()
        println(fields)
        return with(connection) {
            var args = mutableListOf<Any>()
            val statement = createStatement()
            val result = statement.executeQuery(sql)
            while (result.next()) {
//                args.add(result.getObject())
            }
            close()
            type.constructors.first().call(args.toTypedArray())
        }
    }

    fun <T: Entity> select(type: KClass<T>, ) {

    }

}