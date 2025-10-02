package org.marcodossantos.project.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import org.marcodossantos.project.common.getColorsTheme
import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.presentation.ExpensesUIState


private val settings: Settings = Settings()
@Composable
fun ExpensesScreen(
    uiState: ExpensesUIState,
    onExpenseClick: (expense: Expense) -> Unit,
    modifier: Modifier = Modifier) {

    val colors = getColorsTheme()


    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stickyHeader {
            Column(modifier = Modifier.background(colors.backgroundColor)) {
                ExpensesTotalHeader(uiState.total)
                AllExpensesHeader()
            }
        }
        items(uiState.expenses) { expense ->
            ExpensesItem(expense = expense, onExpenseClick = { onExpenseClick(expense) })

        }
    }

}

@Composable
fun ExpensesTotalHeader(total: Double) {
    Card(
        shape = RoundedCornerShape(30),
        modifier = Modifier.background(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(130.dp).background(Color.Black).padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "$$total",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text("USD", modifier = Modifier.align(Alignment.CenterEnd), color = Color.Gray)
        }
    }
}

@Composable
fun AllExpensesHeader() {
    val colors = getColorsTheme()
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "All expenses",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = colors.textColor
        )
        Button(
            shape = RoundedCornerShape(50),
            onClick = {
                //Crear clic
            },
            colors = ButtonColors(
                containerColor = Color.LightGray,
                contentColor = Color.DarkGray,
                Color.LightGray,
                Color.DarkGray
            ),
            enabled = false
        ) {
            Text("View All")
        }
    }
}

@Composable
fun ExpensesItem(expense: Expense, onExpenseClick: (expense: Expense) -> Unit) {
    val colors = getColorsTheme()
    Card(
        modifier = Modifier
            .background(colors.colorExpenseItem)
            .padding(horizontal = 2.dp)
            .clickable {
                onExpenseClick(expense)
            },
        shape = RoundedCornerShape(30)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Surface (modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(35),
                color = colors.purple) {
                Image(
                    modifier = Modifier.padding(10.dp),
                    imageVector = expense.icon,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Imagen icono expense item")
            }
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)){
                Text(
                    text = expense.category.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = colors.textColor
                )
                Text(
                    text = expense.description,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${expense.amount}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colors.textColor
            )
        }
    }

}