package database

import models.Model
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.transactions.transaction
import tables.*
import ui.table.FKTableField

class Postgres {

    private val db = Database.connect(
        "jdbc:postgresql://localhost:5432/appliances_shop",
        driver = "org.postgresql.Driver",
        user = "postgres", password = "root"
    )

    private val fl = { s: String -> "${s.first().uppercase()}." }

    fun deleteCategoryById(categoryId: Int) {
        transaction(db) { Categories.deleteWhere { id eq categoryId } }
    }

    fun deleteProductById(productId: Int) {
        transaction(db) { Product.deleteWhere { id eq productId } }
    }

    fun deleteSaleById(saleId: Int) {
        transaction(db) { Sales.deleteWhere { id eq saleId } }
    }

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

    fun fetchAllSumTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(
                AllSum.id, AllSum.firstname, AllSum.lastname, AllSum.patronymic
            ),
            fetchAllSum(), emptyList()
        )
    }

    fun fetchSaledProductsTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(
                SaledProducts.id, SaledProducts.date, SaledProducts.fio,
                SaledProducts.category, SaledProducts.product, SaledProducts.price
            ),
            fetchSaledProducts(), emptyList()
        )
    }

    fun fetchCategoryTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(Categories.id, Categories.name),
            fetchCategories(), emptyList()
        )
    }

    fun fetchProductTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(Product.id, Product.name, Product.category, Product.price, Product.installationPrice, Product.guaranteePrice),
            fetchProducts(), fetchCategories()
        )
    }

    fun fetchSaleTable(): FKTableField<*, *> = transaction(db) {
        FKTableField(
            listOf(Sales.id, Sales.date, Sales.productId, Sales.firstname, Sales.lastname, Sales.patronymic),
            fetchSales(), fetchProducts()
        )
    }

    private fun fetchAllSum(): List<Model.AllSum> = transaction(db) {
        (Sales innerJoin Product)
            .slice(
                Sales.firstname, Sales.lastname, Sales.patronymic,
                Sales.productId.count(), Product.price.sum()
                        + Product.guaranteePrice.sum()
                        + Product.installationPrice.sum())
            .selectAll()
            .groupBy(Sales.firstname, Sales.lastname, Sales.patronymic)
            .mapIndexed { i, row ->
                Model.AllSum(
                    id = i + 1,
                    fio = "${fl(row[Sales.lastname])} ${fl(row[Sales.firstname])} ${row[Sales.patronymic] ?: ""}",
                    count = row[Sales.productId.count()].toInt(),
                    sum = row[Product.price.sum()
                            + Product.guaranteePrice.sum()
                            + Product.installationPrice.sum()]?.toInt() ?: 0
                )
            }
    }

    private fun fetchSaledProducts(): List<Model.SaledProduct> = transaction(db) {
        fetchSales().mapIndexed { index, sale ->
            Model.SaledProduct(
                id = (index + 1),
                date = sale.date,
                fio = "${fl(sale.lastname)} ${fl(sale.firstname)} ${sale.patronymic}",
                category = sale.product.category,
                product = sale.product,
                price = sale.product.price +
                        (sale.product.guaranteePrice ?: 0) +
                        (sale.product.installationPrice ?: 0)
            )
        }.sortedBy { it.date }
    }

    private fun fetchCategories(): List<Model.Category> = transaction(db) {
        Categories.selectAll().map {
            Model.Category(
                it[Categories.id].value,
                it[Categories.name]
            )
        }
    }

    private fun fetchProducts(): List<Model.Product> = transaction(db) {
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

    private fun fetchSales(query: Query.() -> Query = { this }): List<Model.Sale> {
        return transaction(db) {
            val products = fetchProducts()
            Sales.selectAll().query().map {
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
/**
 *
 * create table Categories
 * (
 *     id   serial primary key not null,
 *     name nchar(50)          not null
 * );
 *
 * create table Product
 * (
 *     id                 serial primary key not null,
 *     name               nchar(50)          not null,
 *     category_id        serial             not null references Categories (id),
 *     price              int                not null,
 *     installation_price int default null,
 *     guarantee_price    int default null
 * );
 *
 * create table Sales
 * (
 *     id                  serial primary key not null,
 *     registration_number int                not null,
 *     date                date               not null,
 *     product_id          serial references Product (id),
 *     firstname           nchar(25)          not null,
 *     lastname            nchar(50)          not null,
 *     patronymic          nchar(50) default null
 * );
 *
 * delete from product where id = 0;
 *
 * select firstname, lastname, patronymic, count(p.id), (sum(p.price) + sum(p.guarantee_price) + sum(p.installation_price)) as sum from sales as s
 * inner join Product as p on s.product_id = p.id
 * group by firstname, lastname, patronymic;
 *
 *
 *
 * **/