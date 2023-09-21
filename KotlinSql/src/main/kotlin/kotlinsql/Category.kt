package kotlinsql

import kotlinsql.annotations.Column
import kotlinsql.annotations.PrimaryKey

data class Category(
    @PrimaryKey var id: Int = 0,
    @Column("name") var categoryName: String = ""
)