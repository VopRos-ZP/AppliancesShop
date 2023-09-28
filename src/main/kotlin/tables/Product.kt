package tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Product : IntIdTable() {
    val name = varchar("name", 50)
    val category = integer("category_id").references(Categories.id)
    val price = integer("price")
    val installationPrice = integer("installation_price").nullable()
    val guaranteePrice = integer("guarantee_price").nullable()
}