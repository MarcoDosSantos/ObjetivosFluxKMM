package org.marcodossantos.project.data

import org.marcodossantos.project.domain.ExpenseRepository
import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory

class ExpenseRepositoryImpl (private val expenseManager: ExpenseManager): ExpenseRepository {
    override fun addNewExpense(expense: Expense) {
        return expenseManager.addNewExpense(expense)
    }

    override fun editExpense(expense: Expense) {
        return expenseManager.editExpense(expense)
    }

    override fun deleteExpense(expense: Expense): List<Expense> {
        return expenseManager.deleteExpense(expense)
    }

    override fun deleteAllExpenses(): List<Expense> {
        return expenseManager.deleteAllExpenses()
    }

    override fun getAllExpenses(): List<Expense> {
        return expenseManager.fakeExpenseList
    }

    override fun getCategories(): List<ExpenseCategory> {
        return expenseManager.getCategories()
    }

    override fun getExpenseById(id: Long): Expense? {
        return expenseManager.getExpenseById(id)
    }
}