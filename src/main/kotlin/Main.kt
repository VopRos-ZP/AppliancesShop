import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun App() {
    // some ui
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
