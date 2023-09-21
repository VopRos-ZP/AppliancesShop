package kotlinsql

import kotlinsql.annotations.Column
import kotlinsql.annotations.PrimaryKey
import java.lang.RuntimeException
import java.sql.Connection
import java.sql.DriverManager

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

    inline fun <reified T: Any, A: Annotation> filterFields(type: Class<T>, annotationType: Class<A>, f: (A) -> String): List<DbField> {
        return type.declaredFields
            .filter { it.isAnnotationPresent(annotationType) }
            .map {
                val annName = when (val n = f(it.getAnnotation(annotationType))) {
                    "" -> it.name
                    else -> n
                }
                DbField(annName = annName, objName = it.name)
            }
            .toList()
    }

    inline fun <reified T: Any> runScript(type: Class<T>, sql: String): List<T> {
        val fields = mutableListOf<DbField>().apply {
            addAll(filterFields(type, PrimaryKey::class.java) { it.name })
            addAll(filterFields(type, Column::class.java) { it.name })
        }
        val list = mutableListOf<T>()
        return with(connection) {
            val items = mutableListOf<Map<DbField, Any>>()
            with(createStatement().executeQuery(sql)) {
                while (next()) { items.add(fields.associateWith { getObject(it.annName) }) }
            }
            close()
            for (item in items) {
                list.add(with(type.constructors.first().newInstance() as T) {
                    javaClass.declaredFields.forEach {
                        it.isAccessible = true
                        it.set(this, item[item.keys.find { db -> db.objName == it.name }])
                    }
                    this
                })
            }
            list
        }
    }

}