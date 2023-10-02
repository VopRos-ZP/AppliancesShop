package ui.table

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ContextMenuItem
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
import models.Model
import org.jetbrains.exposed.sql.Column
import ui.Table

@Composable
fun <T: Model> UITable(
    table: FKTableField<T, *>,
    onDelete: (T) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.animateContentSize(),
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = table.columns.first().table.tableName,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
                textDecoration = TextDecoration.Underline,
            )
            Row {
                IconButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
            Table(
                columnCount = table.columns.size,
                cellWidth = { when (it) {
                    0 -> 50.dp
                    else -> 225.dp
                } },
                data = table.data,
                color = MaterialTheme.colors.primary,
                contextMenuItems = {
                    listOf(
                        ContextMenuItem("Удалить") { onDelete(table.data[it - 1]) },
                        ContextMenuItem("Редактировать") { },
                    )
                },
                headerCellContent = { UITableHeader(it, table.columns) },
                cellContent = { i, item -> UITableCell(i, item, table.columns) }
            )
        }
    }
}

@Composable
fun UITableHeader(index: Int, columns: List<Column<*>>) {
    Text(
        text = columns[index].name,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colors.onPrimary
    )
}

@Composable
fun <T: Model> UITableCell(index: Int, model: T, columns: List<Column<*>>) {
    Text(
        text = "${model.fields[columns[index].name]}",
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colors.onPrimary
    )
}