package org.marcodossantos.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ExpensesApp2",
    ) {
        App()
    }
}