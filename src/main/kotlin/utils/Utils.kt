package utils

import models.Sale

val HEADERS = listOf(
    "№ п/п",
    "ФИО покупателя",
    "Дата продажи",
    "Категория товара",
    "Наименование",
    "Общая стоимость"
)

fun getSaleValueByIndex(index: Int, sale: Sale) = when (index) {
    0 -> "${sale.id}"
    1 -> sale.fio
    2 -> "${sale.date.dayOfMonth}/${sale.date.monthNumber}/${sale.date.year}"
    3 -> sale.product.category.name
    4 -> sale.product.name
    else -> "${sale.product.price}₽"
}