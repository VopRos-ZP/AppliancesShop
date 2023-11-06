package ui.table

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import database.DslReducer
import database.PostgresDsl
import database.pDsl
import models.Model
import ui.Table

@Composable
fun <T: Model> UITable(
    table: FKTableField<T, *>,
    onDelete: PostgresDsl = pDsl {},
    onEdit: PostgresDsl = pDsl {},
    onAddClick: (() -> Unit)? = null
) {
    val reducer = DslReducer()
    Card(
        modifier = Modifier.animateContentSize(),
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = table.data[0]::class.java.simpleName,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
                textDecoration = TextDecoration.Underline,
            )
            AnimatedVisibility(onAddClick != null) {
                Row {
                    if (onAddClick != null) {
                        IconButton(onClick = onAddClick) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
            Table(
                columnCount = table.data[0].fields.size,
                cellWidth = { when (it) {
                    0 -> 50.dp
                    else -> 225.dp
                } },
                data = table.data,
                color = MaterialTheme.colors.primary,
                contextMenuItems = {
                    listOf(
                        ContextMenuItem("Удалить") { reducer.reduce(onDelete.models, table.data[it - 1]) },
                        ContextMenuItem("Редактировать") { reducer.reduce(onEdit.models, table.data[it - 1]) },
                    )
                },
                headerCellContent = { UITableHeader(it, table.data[0].fields) },
                cellContent = { i, item -> UITableCell(i, item, table.data[0].fields) }
            )
        }
    }
}

@Composable
fun UITableHeader(index: Int, columns: Map<String, String>) {
    Text(
        text = columns.keys.elementAt(index),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colors.onPrimary
    )
}

@Composable
fun <T: Model> UITableCell(index: Int, model: T, columns: Map<String, String>) {
    val columnName = columns.keys.elementAt(index)
    Text(
        text = "${model.fields[columnName]}",
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colors.onPrimary
    )
}