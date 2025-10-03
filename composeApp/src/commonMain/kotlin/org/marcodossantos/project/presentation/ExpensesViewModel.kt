package org.marcodossantos.project.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.marcodossantos.project.domain.ExpenseRepository
import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory

class ExpensesViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUIState())
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val list = repository.getAllExpenses()
            _uiState.update { state ->
                state.copy(
                    expenses = list,
                    total = list.sumOf { it.amount }
                )
            }
        }
    }

    fun addNewExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addNewExpense(expense)
            refresh()
        }
    }

    fun editExpense(expense: Expense) {
        viewModelScope.launch {
            repository.editExpense(expense)
            refresh()
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
            refresh()
        }
    }

    fun deleteAllExpenses() {
        viewModelScope.launch {
            repository.deleteAllExpenses()
            refresh()
        }
    }

    fun getExpenseById(id: Long): Expense? {
        return repository.getExpenseById(id)
    }

    fun getCategories(): List<ExpenseCategory> = repository.getCategories()
}

data class ExpensesUIState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0
)
