package org.marcodossantos.project

import android.app.Application
import com.expenseApp.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.marcodossantos.project.data.DatabaseDriverFactory
import org.marcodossantos.project.di.appModule
import org.marcodossantos.project.di.viewModelModule

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@MainApplication)
                androidLogger()
                modules(
                    appModule(
                        AppDatabase.invoke(
                            DatabaseDriverFactory(this@MainApplication).createDriver()
                        )
                    ),
                    viewModelModule
                )
            }
        }
    }
}
