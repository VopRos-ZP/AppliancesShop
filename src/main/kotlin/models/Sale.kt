package models

import kotlinsql.annotations.Column
import kotlinsql.annotations.PrimaryKey
import java.sql.Date
import java.time.LocalDate

data class Sale(
    @PrimaryKey var id: Int = 1,
    @Column("registration_number") var registrationNumber: Int = 0,
    @Column var date: Date = Date.valueOf(LocalDate.now()),
    @Column("product_id") var productId: Int = 1,
    @Column var firstname: String = "",
    @Column var lastname: String = "",
    @Column var patronymic: String = ""
)
