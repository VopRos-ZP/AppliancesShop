package kotlinsql

fun main() {
    Test().reflectionTest()
}

class Test {

    fun reflectionTest() {
        val dbConnection = DbConnection("appliances_shop", "postgres", "VopRos366")
        dbConnection.runScript(Category::class, "SELECT * FROM Categories")
    }

}