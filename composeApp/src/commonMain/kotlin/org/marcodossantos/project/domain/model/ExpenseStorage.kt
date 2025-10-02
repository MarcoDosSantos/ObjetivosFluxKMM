package org.marcodossantos.project.domain.model

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.russhwolf.settings.get
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ExpenseStorage(private val settings: Settings) {

    fun saveExpense(expense: Expense) {
        val json = Json.encodeToString(expense)
        settings.putString("expense_${expense.id}", json)
    }

    fun getExpense(id: Long): Expense? {
        val json = settings.getStringOrNull("expense_$id") ?: return null
        return Json.decodeFromString<Expense>(json)
    }

    fun getAllExpenses(): List<Expense> {
        return settings.keys
            .filter { it.startsWith("expense_") }
            .mapNotNull { key ->
                settings.getStringOrNull(key)?.let { Json.decodeFromString<Expense>(it) }
            }
    }

    fun removeExpense(id: Long) {
        settings.remove("expense_$id")
    }
}
