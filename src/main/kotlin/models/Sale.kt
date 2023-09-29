package models

import kotlinx.datetime.LocalDate

data class Sale(
    val id: Int,
    val date: LocalDate,
    val product: Product,
    val fio: String
)
