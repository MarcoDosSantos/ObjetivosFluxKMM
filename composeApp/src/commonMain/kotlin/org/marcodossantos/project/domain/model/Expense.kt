package org.marcodossantos.project.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.PartyMode
import androidx.compose.material.icons.filled.ViewCozy
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


@Serializable
data class Expense(
    val id: Long = -1,
    val category: ExpenseCategory,
    val amount: Double,
    val description: String
)

@Serializable
enum class ExpenseCategory {
    GROCERIES, PARTY, SNACKS, COFFEE, CAR, HOUSE, OTHER
}

// Enum para UI (no serializable)
enum class ExpenseCategoryIcon(val icon: ImageVector){
    GROCERIES(Icons.Default.FoodBank),
    PARTY(Icons.Default.PartyMode),
    SNACKS(Icons.Default.Fastfood),
    COFFEE(Icons.Default.Coffee),
    CAR(Icons.Default.ElectricCar),
    HOUSE(Icons.Default.House),
    OTHER(Icons.Default.ViewCozy)
}