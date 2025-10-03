package org.marcodossantos.project

import androidx.compose.ui.window.ComposeUIViewController
import com.expenseApp.db.AppDatabase
import org.koin.core.context.startKoin
import org.marcodossantos.project.data.DatabaseDriverFactory
import org.marcodossantos.project.di.appModule


fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(appModule(AppDatabase.invoke(DatabaseDriverFactory().createDriver())))
    }.koin
}
