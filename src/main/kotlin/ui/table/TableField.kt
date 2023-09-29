package ui.table

import models.Model
import models.SClass
import org.jetbrains.exposed.sql.Column

data class TableField<T : Model>(
    val data: T,
    val columns: List<Column<*>>
)