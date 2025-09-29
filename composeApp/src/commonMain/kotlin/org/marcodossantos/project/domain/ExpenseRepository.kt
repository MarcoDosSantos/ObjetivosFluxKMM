package org.marcodossantos.project.domain

import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory

interface ExpenseRepository {
    fun addNewExpense(expense: Expense)
    fun editExpense(expense: Expense)
    fun deleteExpense(expense: Expense): List<Expense>
    fun deleteAllExpenses(): List<Expense>
    fun getAllExpenses(): List<Expense>
    fun getCategories(): List<ExpenseCategory>
    fun getExpenseById(id: Long): Expense?
}