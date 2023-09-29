import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.Database
import ui.table.UITable

@Composable
fun App() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        drawerContent = {}
    ) {

    }
    val database by remember { mutableStateOf(Database()) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
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
        )) { UITable(it) }
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
