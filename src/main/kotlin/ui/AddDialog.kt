package ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import models.Model
import org.jetbrains.exposed.sql.Column
import org.jetbrains.skia.impl.Log
import ui.table.FKTableField
import ui.table.TableField
import ui.table.UITableHeader

@Composable
fun <T: Model> AddDialog(table: FKTableField<T, *>, onClose: () -> Unit) {
    val model by remember { mutableStateOf(table.data) }
    var columns by remember { mutableStateOf(2) }
    val state = rememberDialogState()
    Dialog(
        onCloseRequest = { onClose() },
        state = state
    ) {
        Table(
            columnCount = table.columns.size,
            cellWidth = { when (it) {
                0 -> 50.dp
                else -> 225.dp
            } },
            data = List (columns) { it },
            color = MaterialTheme.colors.primary,
            headerCellContent = { UITableHeader(it, table.columns) },
            cellContent = { _, item -> AddDialogCell(table.columns[item], table.targetData) }
        )
    }
}

@Composable
fun <T: Model> AddDialogCell(column: Column<*>, data: List<T>) {
    when (column.foreignKey) {
        null -> {
            var value by remember { mutableStateOf("") }
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
            )
        }
        else -> {
            var value by remember { mutableStateOf<T?>(null) }
            var expanded by remember { mutableStateOf(false) }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }) {
                data.map {
                    DropdownMenuItem(onClick = { value = it }) {
                        it.fields.map { (_, v) -> Text(v) }
                    }
                }
            }
        }
    }
}