package models

import kotlinx.datetime.LocalDate

sealed class Model(val fields: Map<String, String>) {
    data class Category(
        val id: Int,
        val name: String
    ): Model(mapOf("id" to "$id", "name" to name))
    data class Product(
        val id: Int,
        val name: String,
        val category: Category,
        val price: Int,
        val installationPrice: Int?,
        val guaranteePrice: Int?
    ): Model(mapOf(
        "id" to "$id",
        "name" to name,
        "category_id" to "${category.id}",
        "price" to "$price",
        "installation_price" to "$installationPrice",
        "guarantee_price" to "$guaranteePrice",
    ))
    data class Sale(
        val id: Int,
        val date: LocalDate,
        val product: Product,
        val lastname: String,
        val firstname: String,
        val patronymic: String?
    ): Model(mapOf(
        "id" to "$id",
        "date" to "$date",
        "product_id" to "${product.id}",
        "lastname" to lastname,
        "firstname" to firstname,
        "patronymic" to (patronymic ?: "")
    ))

}