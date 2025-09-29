package org.marcodossantos.project.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import kotlin.invoke

fun initKoin(vararg modules: Module) {
    startKoin {
        modules(modules.toList())
    }
}
