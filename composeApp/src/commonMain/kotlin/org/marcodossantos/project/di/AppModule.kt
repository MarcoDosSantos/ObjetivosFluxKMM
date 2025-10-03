package org.marcodossantos.project.di

import com.expenseApp.db.AppDatabase
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.marcodossantos.project.data.ExpenseManager
import org.marcodossantos.project.data.ExpenseRepositoryImpl
import org.marcodossantos.project.domain.ExpenseRepository
import org.marcodossantos.project.presentation.ExpensesViewModel

fun appModule(appDatabase: AppDatabase) = module {
    single { ExpenseManager }.withOptions { createdAtStart() }
    single<ExpenseRepository> { ExpenseRepositoryImpl(get(), appDatabase) }
    single<ExpenseRepositoryImpl> { get<ExpenseRepository>() as ExpenseRepositoryImpl }
}
val viewModelModule = module {
    factory { ExpensesViewModel(get()) }
}