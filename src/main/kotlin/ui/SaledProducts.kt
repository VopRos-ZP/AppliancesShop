package ui

import androidx.compose.runtime.Composable
import database.Postgres

@Composable
fun SaledProducts(db: Postgres, onClose: () -> Unit) {
    QueryDialog(db, Postgres::fetchSaledProductsTable, onClose)
}

@Composable
fun AllSumProducts(db: Postgres, onClose: () -> Unit) {
    QueryDialog(db, Postgres::fetchAllSumTable, onClose)
}