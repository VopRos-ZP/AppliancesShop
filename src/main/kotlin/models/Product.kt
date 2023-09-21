package models

import kotlinsql.annotations.Column
import kotlinsql.annotations.PrimaryKey

data class Product(
    @PrimaryKey var id: Int = 1,
    @Column var name: String = "",
    @Column("category_id") var categoryId: Int = 1,
    @Column var price: Int,
    @Column("installation_price") var installationPrice: Int,
    @Column("guarantee_price") var guaranteePrice: Int
)
