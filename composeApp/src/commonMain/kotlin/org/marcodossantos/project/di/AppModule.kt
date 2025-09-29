package org.marcodossantos.project.di

import org.koin.dsl.module
import org.marcodossantos.project.data.ExpenseManager
import org.marcodossantos.project.data.ExpenseRepositoryImpl
import org.marcodossantos.project.domain.ExpenseRepository

val appModule = module {
    single { ExpenseManager }
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
}

