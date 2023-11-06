package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import database.Postgres
import ui.table.FKTableField
import ui.table.UITable

@Composable
fun QueryDialog(db: Postgres, init: Postgres.() -> FKTableField<*, *>, onClose: () -> Unit) {
    val models by remember(db) { mutableStateOf(init(db)) }
    val state = rememberDialogState(WindowPosition.PlatformDefault)
    Dialog(onCloseRequest = onClose, state = state) { UITable(table = models) }
}