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
    private val expenses = repository.getAllExpenses()

    init {
        getAllExpenses()
    }

    private fun getAllExpenses() {
        viewModelScope.launch {
            repository.getAllExpenses()
            updateUIState()
        }
    }

    fun addNewExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addNewExpense(expense)
            updateUIState()
        }
    }

    fun editExpense(expense: Expense) {
        viewModelScope.launch {
            repository.editExpense(expense)
            updateUIState()
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
            updateUIState()
        }
    }

    fun deleteAllExpenses() {
        viewModelScope.launch {
            repository.deleteAllExpenses()
            updateUIState()
        }
    }

    fun getExpenseById(id: Long): Expense? {
        return repository.getExpenseById(id)
    }

    private fun updateUIState() {
        _uiState.update { state ->
            state.copy(
                expenses = expenses,
                total = expenses.sumOf { it.amount }
            )
        }
    }

    fun getCategories(): List<ExpenseCategory> {
        return repository.getCategories()
    }
}

data class ExpensesUIState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0
)