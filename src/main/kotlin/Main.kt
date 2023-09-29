import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.AddDialog
import ui.Database
import ui.table.FKTableField
import ui.table.TableField
import ui.table.UITable

@Composable
fun App() {
    val database by remember { mutableStateOf(Database()) }
    var target by remember { mutableStateOf<FKTableField<*, *>?>(null) }
    Box {
        AnimatedVisibility(target != null) {
            target?.let { AddDialog(it) { target = null } }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            item {
                Row {
                    Button(onClick = {}) { Text("Создать запрос") }
                }
            }
            items(listOf(
                database.fetchCategoryTable(),
                database.fetchProductTable(),
                database.fetchSaleTable(),
            )) {
                UITable(it) { t -> target = t }
            }
        }
    }
}

fun main() = application {
    val state = rememberWindowState()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Appliances shop",
        state = state
    ) { MaterialTheme { App() } }
}
