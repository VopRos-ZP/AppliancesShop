package tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.sum

object AllSum : IntIdTable() {
    val firstname = Sales.firstname
    val lastname = Sales.lastname
    val patronymic = Sales.patronymic
    val count = Sales.productId.count()
    val sum = Product.price.sum() + Product.guaranteePrice.sum() + Product.installationPrice.sum()
}