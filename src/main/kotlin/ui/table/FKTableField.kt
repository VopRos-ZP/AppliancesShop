package ui.table

import models.Model
import org.jetbrains.exposed.sql.Column

/**
 *
 * **/
data class FKTableField<T: Model, F: Model>(
    val columns: List<Column<*>>,
    val data: List<T>,
    val targetData: List<F> = emptyList()
)