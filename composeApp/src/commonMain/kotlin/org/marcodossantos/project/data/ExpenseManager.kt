package org.marcodossantos.project.data

import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory
import kotlin.text.category

object ExpenseManager {
    private var currentId = 1L

    val fakeExpenseList = mutableListOf(
        Expense(
            id = currentId++,
            amount = 100.0,
            category = ExpenseCategory.GROCERIES,
            description = "Compra semanal"
        ),
        Expense(
            id = currentId++,
            amount = 10.2,
            category = ExpenseCategory.SNACKS,
            description = "Mostaza"
        ),
        Expense(
            id = currentId++,
            amount = 26.2,
            category = ExpenseCategory.PARTY,
            description = "Cumpleaños"
        ),
        Expense(
            id = currentId++,
            amount = 19.5,
            category = ExpenseCategory.COFFEE,
            description = "Desayuno"
        ),
        Expense(
            id = currentId++,
            amount = 170.0,
            category = ExpenseCategory.CAR,
            description = "Neumáticos"
        ),
        Expense(
            id = currentId++,
            amount = 300.0,
            category = ExpenseCategory.HOUSE,
            description = "Reparación"
        ),
        Expense(
            id = currentId++,
            amount = 100.0,
            category = ExpenseCategory.OTHER,
            description = "Otros"
        )

    )

    fun addNewExpense(expense: Expense){
        fakeExpenseList.add(expense.copy(id = currentId++))
    }

    fun editExpense(expense: Expense){
        val index = fakeExpenseList.indexOfFirst { it.id == expense.id }
        if (index != -1){
            fakeExpenseList[index] = fakeExpenseList[index].copy(
                amount = expense.amount,
                category = expense.category,
                description = expense.description
            )
        }

    }

    fun deleteExpense(expense: Expense): List<Expense> {
        val index = fakeExpenseList.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            fakeExpenseList.removeAt(index)
        }
        return fakeExpenseList.toList() // Devuelve una copia inmutable de la lista
    }

    fun deleteAllExpenses(): List<Expense> {
        fakeExpenseList.clear()
        return fakeExpenseList.toList() // Devuelve una copia inmutable de la lista vacía
    }

    fun getExpenseById(id: Long): Expense? {
        return fakeExpenseList.find { it.id == id }
    }
    fun getExpenseByCategory(category: ExpenseCategory): List<Expense> {
        return fakeExpenseList.filter { it.category == category }
    }
    fun getExpenseByAmount(amount: Double): List<Expense> {
        return fakeExpenseList.filter { it.amount == amount }
    }
    fun getExpenseByDescription(description: String): List<Expense> {
        return fakeExpenseList.filter { it.description == description }
    }
    fun getCategories(): List<ExpenseCategory> {
        return listOf(
            ExpenseCategory.GROCERIES,
            ExpenseCategory.PARTY,
            ExpenseCategory.SNACKS,
            ExpenseCategory.COFFEE,
            ExpenseCategory.CAR,
            ExpenseCategory.HOUSE,
            ExpenseCategory.OTHER
        )
    }
}