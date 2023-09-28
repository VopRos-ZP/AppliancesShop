package models

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Int,
    val installationPrice: Int?,
    val guaranteePrice: Int?
)
