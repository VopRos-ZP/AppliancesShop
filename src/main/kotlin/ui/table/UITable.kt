package ui.table

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ui.Table

@Composable
fun UITable(columns: List<TableField<*>>) {
    Card(
        modifier = Modifier.animateContentSize(),
        backgroundColor = Color.DarkGray,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = columns.first().columns.first().table.tableName,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
                textDecoration = TextDecoration.Underline,
            )
            Row {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Add, contentDescription = null,)
                }
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.Default.Done, contentDescription = null)
                }
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
            Table(
                columnCount = columns.first().columns.size,
                cellWidth = { when (it) {
                    0 -> 50.dp
                    else -> 225.dp
                } },
                data = columns,
                color = Color.DarkGray,
                headerCellContent = { UITableHeader(it, columns) },
                cellContent = { i, item -> UITableCell(i, item) }
            )
        }
    }
}

@Composable
fun UITableHeader(index: Int, fields: List<TableField<*>>) {
    Text(
        text = fields.first().columns[index].name,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
fun UITableCell(index: Int, field: TableField<*>) {
    Text(
        text = "${field.data.fields[field.columns[index].name]}",
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}