package ui

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import kotlinx.datetime.LocalDate
import models.Model
import org.jetbrains.exposed.sql.*
import ui.table.FKTableField
import ui.table.UITableHeader
import utils.toLocalDate

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Model> AddDialog(
    table: FKTableField<T, *>,
    onClose: (List<T>) -> Unit
) {
    AddDialog(
        table = table,
        onSave = {
                 when (table.data.first()) {
                     is Model.Category -> Model.Category(
                         id = it["id"]?.toInt() ?: 0,
                         name = it["name"] ?: ""
                     ) as T
                     is Model.Product -> Model.Product(
                         id = it["id"]?.toInt() ?: 0,
                         name = it["name"] ?: "",
                         category = table.targetData.first { t -> t.fields["id"] == (it["category_id"].let { c -> if (c.isNullOrEmpty()) "1" else c }) } as Model.Category,
                         price = it["price"]?.toInt() ?: 0,
                         installationPrice = it["installation_price"]?.toInt(),
                         guaranteePrice = it["guarantee_price"]?.toInt()
                     ) as T
                     is Model.Sale -> Model.Sale(
                         id = it["id"]?.toInt() ?: 0,
                         date = it["date"]?.toLocalDate() ?: java.time.LocalDate.now().let { d -> LocalDate(d.year, d.month, d.dayOfMonth) },
                         product = table.targetData.first { t -> t.fields["id"] == (it["product_id"].let { c -> if (c.isNullOrEmpty()) "1" else c }) } as Model.Product,
                         lastname = it["lastname"] ?: "",
                         firstname = it["firstname"] ?: "",
                         patronymic = it["patronymic"],
                     ) as T
                     else -> null
                 }
        },
        onClose = { onClose(it) }
    )
}

@Composable
fun <T: Model> AddDialog(
    table: FKTableField<T, *>,
    onSave: (Map<String, String>) -> T?,
    onClose: (List<T>) -> Unit
) {
    val state = rememberDialogState(WindowPosition.PlatformDefault)
    val results = remember { mutableStateListOf(mutableStateMapOf<String, String>()) }
    Dialog(
        onCloseRequest = { onClose(emptyList()) },
        state = state
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                IconButton(onClick = { results.add(mutableStateMapOf()) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
                Table(
                    columnCount = table.data.size,
                    cellWidth = { when (it) {
                        0 -> 75.dp
                        else -> 225.dp
                    } },
                    data = results,
                    color = MaterialTheme.colors.primary,
                    contextMenuItems = {
                        listOf(ContextMenuItem("Удалить строку") { results.removeAt(it - 1) })
                    },
                    headerCellContent = { UITableHeader(it, table.data[0].fields) },
                    cellContent = { i, res -> AddDialogCell(table.columns[i], table.targetData) {
                        res[table.data[0].fields.keys.elementAt(i)] = it
                    } }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    onClick = { onClose(emptyList()) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) { Text("Назад") }
                Button(onClick = { onClose(results.mapNotNull(onSave)) }) { Text("Сохранить") }
            }
        }
    }
}

@Composable
fun <T: Model> AddDialogCell(column: Column<*>, data: List<T>, onValueChange: (String) -> Unit) {
    when (column.referee) {
        null -> TextureField(column, onValueChange)
        else -> DropDownField(data, onValueChange)
    }
}

@Composable
fun TextureField(column: Column<*>, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    val type = when (column.columnType) {
        is IntegerColumnType -> KeyboardType.Number
        is EntityIDColumnType<*> -> KeyboardType.Number
        else -> KeyboardType.Text
    }
    Box(modifier = Modifier.padding(10.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = when (type) {
                    KeyboardType.Number -> try {
                        "${it.toInt()}"
                    } catch (_: Exception) {
                        it.dropLast(1)
                    }

                    else -> it
                }
                onValueChange(value)
            },
            keyboardOptions = KeyboardOptions(keyboardType = type),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colors.onPrimary,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                focusedBorderColor = MaterialTheme.colors.onPrimary,
                textColor = MaterialTheme.colors.onPrimary
            )
        )
    }
}

@Composable
fun <T: Model> DropDownField(data: List<T>, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf<T?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val id = value?.fields?.get("id") ?: ""
    LaunchedEffect(value) { onValueChange(id) }
    Box(modifier = Modifier.padding(10.dp)) {
        OutlinedTextField(
            value = id,
            onValueChange = {},
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