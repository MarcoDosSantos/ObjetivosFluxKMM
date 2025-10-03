package org.marcodossantos.project.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.expenseApp.db.AppDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory (private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "AppDatabase.db")
    }
}