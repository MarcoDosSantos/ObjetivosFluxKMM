package org.marcodossantos.project.data

import com.expenseApp.db.AppDatabase
import org.marcodossantos.project.domain.ExpenseRepository
import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory

class ExpenseRepositoryImpl(
    private val expenseManager: ExpenseManager,
    private val appDatabase: AppDatabase
) : ExpenseRepository {

    private val queries = appDatabase.expensesDbQueries

    override fun addNewExpense(expense: Expense) {
        queries.transaction{
            queries.insert(
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description
            )
        }
    }

    override fun editExpense(expense: Expense) {
        queries.transaction{
            queries.update(
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description,
                id = expense.id
            )
        }

    }

    override fun deleteExpense(expense: Expense): List<Expense> {
        return expenseManager.deleteExpense(expense)
    }

    override fun deleteAllExpenses(): List<Expense> {
        return expenseManager.deleteAllExpenses()
    }

    override fun getAllExpenses(): List<Expense> {
        return queries.selectAll().executeAsList().map {
            Expense(
                id = it.id,
                amount = it.amount,
                category = ExpenseCategory.valueOf(it.categoryName),
                description = it.description ?: ""
            )
        }

    }

    override fun getCategories(): List<ExpenseCategory> {
        return queries.categories().executeAsList().map {
            ExpenseCategory.valueOf(it)
        }

    }

    override fun getExpenseById(id: Long): Expense? {
        return queries.selectById(id).executeAsOneOrNull()?.let {
            Expense(
                id = it.id,
                amount = it.amount,
                category = ExpenseCategory.valueOf(it.categoryName),
                description = it.description ?: ""
            )
        }
    }

}