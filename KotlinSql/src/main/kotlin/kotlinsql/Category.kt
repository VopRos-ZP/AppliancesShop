package kotlinsql

data class Category(
    override val id: Int,
    val name: String
): Entity(id)