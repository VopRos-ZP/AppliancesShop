package kotlinsql

fun main() {
    Test().reflectionTest()
}

class Test {

    fun reflectionTest() {
        val dbConnection = DbConnection("appliances_shop", "postgres", "root") // VopRos366 for Win
        println(dbConnection.runScript(Category::class.java, sql = "SELECT * FROM Categories"))
    }

}