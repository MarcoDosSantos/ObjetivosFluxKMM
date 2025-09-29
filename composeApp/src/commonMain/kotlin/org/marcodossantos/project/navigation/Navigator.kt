package org.marcodossantos.project.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.viewmodel.viewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.marcodossantos.project.common.getColorsTheme
import org.marcodossantos.project.data.ExpenseManager
import org.marcodossantos.project.data.ExpenseRepositoryImpl
import org.marcodossantos.project.presentation.ExpensesViewModel
import org.marcodossantos.project.presentation.ui.ExpensesDetailScreen
import org.marcodossantos.project.presentation.ui.ExpensesScreen

@Composable
fun Navigation(navigator: Navigator, modifier: Modifier) {
    val colors = getColorsTheme()
    val viewModel = viewModel(modelClass = ExpensesViewModel::class) {
        ExpensesViewModel(ExpenseRepositoryImpl(ExpenseManager))
    }
    NavHost(
        modifier = Modifier.background(colors.backgroundColor),
        navigator = navigator,
        initialRoute = "/home"
    ) {
        scene (route = "/home"){
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ExpensesScreen(
                uiState = uiState,
                onExpenseClick = { expense -> navigator.navigate(route = "/addExpenses/${expense.id}?") },
                modifier = modifier
            )
        }
        scene (route = "/addExpenses/{id}?"){
            val idFromPath = it.path<Long>("id")
            val expenseToEditOrAdd = idFromPath?.let { id -> viewModel.getExpenseById(id)}

            ExpensesDetailScreen(expenseToEdit = expenseToEditOrAdd, categoryList = viewModel.getCategories()){ expense ->
                if (expenseToEditOrAdd == null){
                    viewModel.addNewExpense(expense)
                }else{
                    viewModel.editExpense(expense)
                }
                navigator.popBackStack()
            }
        }
    }
}