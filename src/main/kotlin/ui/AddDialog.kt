package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import models.Model
import org.jetbrains.exposed.sql.*
import ui.table.FKTableField
import ui.table.UITableHeader

@Composable
fun <T: Model> AddDialog(table: FKTableField<T, *>, onClose: (Model?) -> Unit) {
    val state = rememberDialogState(width = 1000.dp, height = 500.dp)
    var result by remember { mutableStateOf<Model?>(null) }
    val map = remember { mutableStateMapOf<String, String>() }
    LaunchedEffect(map) {
        println("map -> $map")
        println("res -> $result")
        result = result?.copy(map)
    }
    Dialog(
        onCloseRequest = { onClose(result) },
        state = state
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Table(
                columnCount = table.columns.size,
                cellWidth = { when (it) {
                    0 -> 75.dp
                    else -> 225.dp
                } },
                data = List(1) { it },
                color = MaterialTheme.colors.primary,
                headerCellContent = { UITableHeader(it, table.columns) },
                cellContent = { i, _ -> AddDialogCell(table.columns[i], table.targetData) { map[table.columns[i].name] = it } }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    onClick = { onClose(result) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) { Text("Назад") }
                Button(onClick = { onClose(result) }) { Text("Сохранить") }
            }
        }
    }
}

@Composable
fun <T: Model> AddDialogCell(column: Column<*>, data: List<T>, onValueChange: (String) -> Unit) {
    when (column.referee) {
        null -> {
            var value by remember { mutableStateOf("") }
            val type = when (column.columnType) {
                is IntegerColumnType -> KeyboardType.Number
                is EntityIDColumnType<*> -> KeyboardType.Number
                else -> KeyboardType.Text
            }
            LaunchedEffect(value) { onValueChange(value) }
            OutlinedTextField(
                value = value,
                onValueChange = {
                    value = when (type) {
                        KeyboardType.Number -> try { "${it.toInt()}" } catch (_: Exception) { it.dropLast(1) }
                        else -> it
                    }
                                },
                keyboardOptions = KeyboardOptions(keyboardType = type),
                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.onPrimary)
            )
        }
        else -> {
            var value by remember { mutableStateOf<T?>(null) }
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = value?.fields?.get("id") ?: "",
                    onValueChange = { onValueChange(it) },
                    readOnly = true,
                    trailingIcon = { IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colors.secondary)
                    } },
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.onPrimary)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    data.map {
                        DropdownMenuItem(onClick = { value = it }) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { it.fields.map { (_, v) -> Text(text = v) } }
                        }
                    }
                }
            }
        }
    }
}