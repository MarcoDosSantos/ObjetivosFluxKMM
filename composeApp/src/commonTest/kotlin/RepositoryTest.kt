import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.marcodossantos.project.di.appModule
import org.marcodossantos.project.data.ExpenseManager
import org.marcodossantos.project.data.ExpenseRepositoryImpl
import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RepositoryTest : KoinTest {
    private val repo by inject<ExpenseRepositoryImpl>()
    private val expenseManager by inject<ExpenseManager>()

    init {
        startKoin { modules(appModule) }
    }

    @Test
    fun expense_list_is_not_empty(){
        //Given/Arrange
        val expensesList = mutableListOf<Expense>()
        //When/Act
        expensesList.addAll(repo.getAllExpenses())
        //Then/Assert
        assertTrue(expensesList.isNotEmpty())
    }

    @Test
    fun add_new_expense(){
        //Given/Arrange
        val expensesList = repo.getAllExpenses()
        //When/Act
        repo.addNewExpense(Expense(amount = 4.5, category = ExpenseCategory.OTHER, description = "Combustible"))
        //Then/Assert
        assertContains(expensesList, expensesList.find { it.id == 8L })
    }

    @Test
    fun edit_expense(){
        //Given/Arrange
        val expensesListBeforeEdition = repo.getAllExpenses()
        //When/Act
        val newExpenseId = 8L
        repo.addNewExpense(Expense(amount = 4.5, category = ExpenseCategory.OTHER, description = "Combustible"))
        //Then/Assert
        assertNotNull(expensesListBeforeEdition.find { it.id == newExpenseId })
        //When/Act
        val updatedExpense = Expense(id = newExpenseId, amount = 8.0, category = ExpenseCategory.OTHER, description = "Ropa")
        repo.editExpense(updatedExpense)
        val expensesListAfterEdition = repo.getAllExpenses()
        //Then/Assert
        assertEquals(updatedExpense, expensesListAfterEdition.find { it.id == newExpenseId })
    }

    @Test
    fun get_all_categories(){
        //Given/Arrange
        val categoriesList = mutableListOf<ExpenseCategory>()
        //When/Act
        categoriesList.addAll(repo.getCategories())
        //Then/Assert
        assertTrue(categoriesList.isNotEmpty())
    }

    @Test
    fun check_all_categories (){
        //Given/Arrange
        val allCategories = listOf(
            ExpenseCategory.GROCERIES,
            ExpenseCategory.PARTY,
            ExpenseCategory.SNACKS,
            ExpenseCategory.COFFEE,
            ExpenseCategory.CAR,
            ExpenseCategory.HOUSE,
            ExpenseCategory.OTHER
        )
        val repoCategories = repo.getCategories()
        //When/Act
        //Then/Assert
        assertEquals(allCategories, repoCategories)
    }

}