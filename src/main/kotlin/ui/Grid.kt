package ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import utils.HEADERS

@Composable
inline fun <reified T> GridViewContent(
    items: List<T>,
    noinline content: @Composable (Int, T) -> Unit
) {
    Table(
        columnCount = items.first()!!::class.java.declaredFields.size,
        cellWidth = { index ->
            when (index) {
                0 -> 100.dp
                1 -> 400.dp
                2 -> 175.dp
                3 -> 250.dp
                4 -> 250.dp
                else -> 100.dp
            }
        },
        data = items,
        headerCellContent = { HeaderSaleItem(it) },
        cellContent = content
    )
}

@Composable
fun HeaderSaleItem(index: Int) {
    Text(
        text = HEADERS[index],
        textAlign = TextAlign.Center,
        maxLines = 1,
        modifier = Modifier.padding(vertical = 10.dp),
        fontWeight = FontWeight.Black,
        textDecoration = TextDecoration.Underline
    )
}