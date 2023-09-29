package models

data class Product(
    val id: Int,
    val name: String,
    val category: Category,
    val price: Int,
    val installationPrice: Int?,
    val guaranteePrice: Int?
)
