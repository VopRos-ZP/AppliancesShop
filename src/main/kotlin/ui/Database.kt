package ui

import models.Model
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import tables.Categories
import tables.Product
import tables.Sales
import ui.table.TableField

class Database {

    private val db = Database.connect(
        "jdbc:postgresql://localhost:5432/appliances_shop",
        driver = "org.postgresql.Driver",
        user = "postgres", password = "root"
    )

    fun fetchCategoryTable(): List<TableField<*>> = transaction(db) {
        fetchCategories().map {
            TableField(it, listOf(Categories.id, Categories.name))
        }
    }

    fun fetchProductTable(): List<TableField<*>> = transaction(db) {
        fetchProducts().map {
            TableField(it, listOf(
                Product.id, Product.name,
                Product.category, Product.price,
                Product.installationPrice,
                Product.guaranteePrice
            ))
        }
    }

    fun fetchSaleTable(): List<TableField<*>> = transaction(db) {
        fetchSales().map {
            TableField(it, listOf(
                Sales.id, Sales.date,
                Sales.productId, Sales.lastname,
                Sales.firstname, Sales.patronymic
            ))
        }
    }

    fun fetchCategories(): List<Model.Category> = transaction(db) {
        Categories.selectAll().map {
            Model.Category(
                it[Categories.id].value,
                it[Categories.name]
            )
        }
    }

    fun fetchProducts(): List<Model.Product> = transaction(db) {
        val categories = fetchCategories()
        Product.selectAll().map {
            Model.Product(
                id = it[Product.id].value,
                name = it[Product.name],
                category = categories.first { c -> c.id == it[Product.category] },
                price = it[Product.price],
                installationPrice = it[Product.installationPrice],
                guaranteePrice = it[Product.guaranteePrice]
            )
        }
    }

    fun fetchSales(): List<Model.Sale> {
        return transaction(db) {
            val products = fetchProducts()
            Sales.selectAll().map {
                Model.Sale(
                    id = it[Sales.id].value,
                    date = it[Sales.date],
                    product = products.first { p -> p.id == it[Sales.productId] },
                    lastname = it[Sales.lastname].trim(),
                    firstname = it[Sales.firstname].trim(),
                    patronymic = it[Sales.patronymic]?.trim()
                )
            }
        }
    }

}