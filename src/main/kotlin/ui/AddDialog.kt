package ui

import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import models.Sale
import utils.getSaleValueByIndex

@Composable
fun <T> AddDialog(t: T, onClose: (T) -> Unit) {
    val saleState by remember { mutableStateOf(t) }
    Dialog(
        onCloseRequest = { onClose(saleState) },
        transparent = true,
        resizable = false,
    ) {

    }
}

@Composable
fun SaleTextField(index: Int, sale: Sale) {
    var value by remember { mutableStateOf(getSaleValueByIndex(index, sale)) }

    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
    )
}