import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import models.*
import ui.HomeViewModel

@Composable
fun App() {
    val sales = remember { mutableStateListOf<Sale>() }
    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton({
                sales.clear()
                sales.addAll(fetchSalesFromDb())
            }) {
                Icon(Icons.Default.List, contentDescription = null)
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("№ п/п")
                    Text("Дата продажи")
                    Text("ФИО покупателя")
                    Text("Категория товара")
                    Text("Наименование")
                    Text("Общая стоимость")
                }
            }
            items(sales) { SaleRow(it) }
        }
    }
    LaunchedEffect(null) {
        sales.clear()
        sales.addAll(fetchSalesFromDb())
    }
}

@Composable
fun SaleRow(sale: Sale) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("${sale.id}")
        Text("${sale.date}")
        Text(sale.fio)
        Text(sale.product.category)
        Text(sale.product.name)
        Text("${sale.product.price}")
    }
}

private fun fetchSalesFromDb(): List<Sale> {
    return HomeViewModel().fetch()
}
//val dbConnection = DbConnection("appliances_shop", "postgres", "root") // VopRos366 for Win
//println(dbConnection.runScript(Category::class.java, sql = "SELECT * FROM Categories"))

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            App()
        }
    }
}
