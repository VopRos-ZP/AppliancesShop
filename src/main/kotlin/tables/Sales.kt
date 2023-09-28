package tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object Sales : IntIdTable() {
    val date = date("date")
    val productId = integer("product_id").references(Product.id)
    val firstname = varchar("firstname", 25)
    val lastname = varchar("lastname", 50)
    val patronymic = varchar("patronymic", 50).nullable()
}