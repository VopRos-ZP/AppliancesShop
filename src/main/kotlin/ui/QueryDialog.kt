package ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import java.util.Scanner

@Composable
fun QueryDialog(onClose: () -> Unit) {



    Dialog(onCloseRequest = { onClose() }) {

    }
}