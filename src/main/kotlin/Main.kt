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
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import database.Postgres
import database.pDsl
import models.Model
import ui.AddDialog
import ui.AllSumProducts
import ui.SaledProducts
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

    var showSaled by remember { mutableStateOf(false) }
    var showAllSum by remember { mutableStateOf(false) }

    Box {
        AnimatedVisibility(target != null) {
            target?.let {
                AddDialog(it) { ms ->
                    ms.forEach { m ->
                        when (m) {
                            is Model.Category -> database.insertCategory(m)
                            is Model.Product -> database.insertProduct(m)
                            is Model.Sale -> database.insertSale(m)
                            else -> {}
                        }
                        database = Postgres()
                        target = null
                    }
                }
            }
        }
        AnimatedVisibility(showSaled) {
            SaledProducts(database) { showSaled = false }
        }
        AnimatedVisibility(showAllSum) {
            AllSumProducts(database) { showAllSum = false }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Button(onClick = { showSaled = true }) { Text("Приобретенные товары") }
                    Button(onClick = { showAllSum = true }) { Text("Общая сумма покупателя") }
                }
            }
            items(listOf(categories, products, sales)) {
                Box(modifier = Modifier.animateItemPlacement()) {
                    UITable(it,
                        onDelete = pDsl {
                            onModel<Model.Category> { database.deleteCategoryById(it.id) }
                            onModel<Model.Product> { database.deleteProductById(it.id) }
                            onModel<Model.Sale> { database.deleteSaleById(it.id) }
                            database = Postgres()
                        },
                        onEdit = pDsl {
                            onModel<Model.Category> { database.insertCategory(it) }
                            onModel<Model.Product> { database.insertProduct(it) }
                            onModel<Model.Sale> { database.insertSale(it) }
                            database = Postgres()
                        }
                    ) { target = it }
                }
            }
        }
    }
}

fun main() = application {
    val state = rememberWindowState(WindowPlacement.Fullscreen)
    Window(
        onCloseRequest = ::exitApplication,
        title = "Appliances shop",
        state = state
    ) { MaterialTheme { App() } }
}
