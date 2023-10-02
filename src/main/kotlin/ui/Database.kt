package ui

import models.Model
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import tables.Categories
import tables.Product
import tables.Sales
import ui.table.FKTableField
import ui.table.TableField

class Database {

    private val db = Database.connect(
        "jdbc:postgresql://localhost:5432/appliances_shop",
        driver = "org.postgresql.Driver",
        user = "postgres", password = "VopRos366"
    )

    fun insertCategory(category: Model.Category) {
        transaction(db) {
            Categories.insert {
                it[id] = category.id
                it[name] = category.name
            }
        }
    }

    fun insertProduct(product: Model.Product) {
        transaction(db) {
            Product.insert {
                it[id] = product.id
                it[name] = product.name
                it[category] = product.category.id
                it[price] = product.price
                it[installationPrice] = product.installationPrice
                it[guaranteePrice] = product.guaranteePrice
            }
        }
    }

    fun insertSale(sale: Model.Sale) {
        transaction(db) {
            Sales.insert {
                it[id] = sale.id
                it[date] = sale.date
                it[productId] = sale.product.id
                it[lastname] = sale.lastname
                it[firstname] = sale.firstname
                it[patronymic] = sale.patronymic
            }
        }
    }

    fun fetchCategoryTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(Categories.id, Categories.name),
            fetchCategories(), emptyList()
        )
    }

    fun fetchProductTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(
                Product.id, Product.name,
                Product.category, Product.price,
                Product.installationPrice,
                Product.guaranteePrice
            ),
            fetchProducts(),
            fetchCategories()
        )
    }

    fun fetchSaleTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(
                Sales.id, Sales.date,
                Sales.productId, Sales.lastname,
                Sales.firstname, Sales.patronymic
            ),
            fetchSales(),
            fetchProducts()
        )
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