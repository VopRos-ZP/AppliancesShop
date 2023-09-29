package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*

/**
 * The horizontally scrollable table with header and content.
 * @param columnCount the count of columns in the table
 * @param cellWidth the width of column, can be configured based on index of the column.
 * @param data the data to populate table.
 * @param modifier the modifier to apply to this layout node.
 * @param headerCellContent a block which describes the header cell content.
 * @param cellContent a block which describes the cell content.
 */
@Composable
fun <T> Table(
    columnCount: Int,
    cellWidth: (index: Int) -> Dp,
    data: List<T>,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    headerCellContent: @Composable (index: Int) -> Unit,
    cellContent: @Composable (index: Int, item: T) -> Unit,
) {
    Surface(
        modifier = modifier.scrollable(rememberScrollState(), Orientation.Horizontal),
        color = Color.Transparent,
    ) {
        LazyRow(modifier = Modifier.background(Color.Transparent)) {
            items((0 until columnCount).toList()) { columnIndex ->
                Column(modifier = Modifier.background(Color.Transparent)) {
                    (0..data.size).forEach { index ->
                        Surface(
                            border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
                            contentColor = Color.Transparent,
                            color = color,
                            modifier = Modifier.width(cellWidth(columnIndex))
                        ) {
                            when (index == 0) {
                                true -> headerCellContent(columnIndex)
                                false -> cellContent(columnIndex, data[index - 1])
                            }
                        }
                    }
                }
            }
        }
    }
}
