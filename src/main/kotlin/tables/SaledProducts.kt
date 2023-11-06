package tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object SaledProducts : IntIdTable() {
    val date = date("date")
    val fio = varchar("fio", length = 150)
    val category = integer("category_id").references(Categories.id)
    val product = integer("product_id").references(Product.id)
    val price = integer("price")
}