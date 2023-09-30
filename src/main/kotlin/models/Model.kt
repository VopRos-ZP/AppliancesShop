package models

import kotlinx.datetime.LocalDate
import kotlin.reflect.full.createInstance

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

    fun copy(args: Map<String, String>): Model {
        return when (val instance = this) {
            is Category -> instance.copy(
                id = args["id"]?.toInt() ?: 0,
                name = args["name"] ?: ""
            )
            is Product -> instance.copy(
                id = args["id"]?.toInt() ?: 0,
                name = args["name"] ?: "",
                category = instance.category.copy(),
                price = args["price"]?.toInt() ?: 0
            )
            is Sale -> instance.copy()
        }
    }

}