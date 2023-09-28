package ui

import models.Sale
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import tables.Categories
import tables.Product
import tables.Sales

class HomeViewModel {

    fun fetch(): List<Sale> {
        val db = Database.connect(
            "jdbc:postgresql://localhost:5432/appliances_shop",
            driver = "org.postgresql.Driver",
            user = "postgres", password = "VopRos366"
        )
        return transaction(db) {
            val categories = Categories.selectAll().associate { it[Categories.id].value to it[Categories.name] }
            val products = Product.selectAll().associate {
                it[Product.id].value to models.Product(
                    id = it[Product.id].value,
                    name = it[Product.name],
                    category = categories[it[Product.category]]!!,
                    price = it[Product.price],
                    installationPrice = it[Product.installationPrice],
                    guaranteePrice = it[Product.guaranteePrice]
                )
            }
            Sales.selectAll().map {
                Sale(
                    id = it[Sales.id].value,
                    date = it[Sales.date],
                    product = products[it[Sales.productId]]!!,
                    fio = "${it[Sales.lastname]} ${it[Sales.firstname]} ${it[Sales.patronymic] ?: ""}".trim()
                )
            }
        }
    }

}