import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ModelTest {
    @Test
    fun expense_model_list_test(){
        //Given/Arrange
        val expenseList = mutableListOf<Expense>()
        val expenses = Expense(id = 1, amount = 4.5, category = ExpenseCategory.CAR, description = "Combustible")
        //When/Act
        expenseList.add(expenses)
        //Then/Assert
        assertContains(expenseList, expenses)
    }
@Test
    fun expense_model_param_test_success(){
        //Given/Arrange
        val expenseList = mutableListOf<Expense>()
        val expense1 = Expense(id = 1, amount = 4.5, category = ExpenseCategory.OTHER, description = "Combustible")
        val expense2 = Expense(id = 2, amount = 24.5, category = ExpenseCategory.OTHER, description = "Limpieza")
        //When/Act
        expenseList.add(expense1)
        expenseList.add(expense2)
        //Then/Assert
        assertEquals(expense1.category, expense2.category)
    }
    @Test
    fun expense_model_param_test_not_equal_success(){
        //Given/Arrange
        val expenseList = mutableListOf<Expense>()
        val expense1 = Expense(id = 1, amount = 4.5, category = ExpenseCategory.CAR, description = "Combustible")
        val expense2 = Expense(id = 2, amount = 24.5, category = ExpenseCategory.OTHER, description = "Limpieza")
        //When/Act
        expenseList.add(expense1)
        expenseList.add(expense2)
        //Then/Assert
        assertNotEquals(expense1.category, expense2.category)
    }
}