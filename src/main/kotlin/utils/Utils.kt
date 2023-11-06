package utils

import kotlinx.datetime.LocalDate

fun String.toLocalDate(): LocalDate = split("/", "-", ".").map { it.toInt() }.let {
    LocalDate(it[0], it[1], it[2])
}
