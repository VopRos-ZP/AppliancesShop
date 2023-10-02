import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import database.Postgres
import models.Model
import ui.AddDialog
import ui.table.FKTableField
import ui.table.UITable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
    var database by remember { mutableStateOf(Postgres()) }
    var target by remember { mutableStateOf<FKTableField<*, *>?>(null) }

    val categories by remember(database) { mutableStateOf(database.fetchCategoryTable()) }
    val products by remember(database) { mutableStateOf(database.fetchProductTable()) }
    val sales by remember(database) { mutableStateOf(database.fetchSaleTable()) }

    Box {
        AnimatedVisibility(target != null) {
            target?.let { AddDialog(it) { ms ->
                ms.forEach { m ->
                    println("m = $m")
                    when (m) {
                        is Model.Category -> database.insertCategory(m)
                        is Model.Product -> database.insertProduct(m)
                        is Model.Sale -> database.insertSale(m)
                    }
                }
                database = Postgres()
                target = null
            } }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            item { Row { Button(onClick = {}) { Text("Создать запрос") } } }
            items(listOf(categories, products, sales)) {
                Box(modifier = Modifier.animateItemPlacement()) {
                    UITable(it, { m ->
                        when (m) {
                            is Model.Category -> database.deleteCategoryById(m.id)
                            is Model.Product -> database.deleteProductById(m.id)
                            is Model.Sale -> database.deleteSaleById(m.id)
                        }
                        database = Postgres()
                    }) { target = it }
                }
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
