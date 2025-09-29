package org.marcodossantos.project.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.marcodossantos.project.common.getColorsTheme
import org.marcodossantos.project.data.TitleTopBarTypes
import org.marcodossantos.project.domain.model.Expense
import org.marcodossantos.project.domain.model.ExpenseCategory
import kotlin.compareTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesDetailScreen(
    expenseToEdit: Expense? = null,
    categoryList: List<ExpenseCategory> = emptyList(),
    addExpenseAndNavigateBack: (expense: Expense) -> Unit
) {
    val colors = getColorsTheme()
    var price by remember { mutableStateOf(expenseToEdit?.amount ?: 0.0) }
    var description by remember { mutableStateOf(expenseToEdit?.description ?: "") }
    var expenseCategory by remember { mutableStateOf(expenseToEdit?.category?.name ?: "") }
    var categorySelected by remember {
        mutableStateOf(
            expenseToEdit?.category?.name ?: "Select a category"
        )
    }

    // --- Lógica para ModalBottomSheet de Material 3 ---
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Efecto para ocultar el teclado cuando el BottomSheet se muestra
    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            keyboardController?.hide()
        }
    }

    // --- **CAMBIO PRINCIPAL: Usar Scaffold para estructurar la pantalla** ---
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (expenseToEdit != null) "Edit Expense" else "Add Expense"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.backgroundColor, // Color de fondo de la TopAppBar
                    titleContentColor = colors.textColor // Color del título
                )
            )
        }
    ){ innerPadding ->
        // Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .padding(innerPadding) // **Aplica el padding del Scaffold**
                .padding(horizontal = 16.dp) // Tu padding horizontal
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // **Añadido para evitar desbordamiento en pantallas pequeñas**
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Espacio superior
            ExpenseAmount(
                priceContent = price,
                onPriceChange = {
                    price = it
                },
                keyboardController = keyboardController
            )
            Spacer(modifier = Modifier.height(30.dp))
            ExpenseTypeSelector(
                categorySelected = categorySelected,
                openBottomSheet = {
                    showBottomSheet = true
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            ExpenseDescription(
                descriptionContent = description,
                onDescriptionChange = {
                    description = it
                },
                keyboardController = keyboardController
            )
            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Espacio inferior para el botón
                onClick = {
                    val expense = Expense(
                        amount = price,
                        description = description,
                        category = ExpenseCategory.valueOf(expenseCategory)
                    )
                    val expenseFromEdit = expenseToEdit?.id?.let { expense.copy(id = it) }
                    addExpenseAndNavigateBack(expenseFromEdit ?: expense)
                },
                shape = RoundedCornerShape(45), // El clip es menos idiomático que shape para Button
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.purple,
                    contentColor = Color.White
                ),
                enabled = price != 0.0 && description.isNotBlank() && expenseCategory.isNotBlank()
            ) {
                Text(
                    text = if (expenseToEdit != null) TitleTopBarTypes.EDIT.value else TitleTopBarTypes.ADD.value
                )
            }
        }
    }

    // --- ModalBottomSheet de Material 3 (se muestra condicionalmente sobre el Scaffold) ---
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            CategoryBottomSheetContent(categories = categoryList) { category ->
                categorySelected = category.name
                expenseCategory = category.name
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        }
    }
}

// ... El resto de tus Composables (ExpenseAmount, ExpenseTypeSelector, etc.) permanecen igual ...
// (El código de las otras funciones no necesita cambios)

@OptIn(ExperimentalMaterial3Api::class) // Necesario para TextFieldDefaults
@Composable
private fun ExpenseAmount(
    priceContent: Double,
    onPriceChange: (Double) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    val colors = getColorsTheme()
    // Normalizar el estado inicial para evitar ".0" en números enteros
    var text by remember { mutableStateOf(if (priceContent == 0.0) "" else priceContent.toString()) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Amount",
            fontSize = 20.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Este TextField reemplaza la combinación Text+TextField para una mejor UX
            TextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = { newText ->
                    // Permite un solo punto decimal
                    val numericText = newText.filter { it.isDigit() || it == '.' }
                    // --- CORRECCIÓN AQUÍ ---
                    if (numericText.count { it == '.' } <= 1) { // Cambiar 'compareTo 1' por '<= 1'
                        text = numericText
                        // Evita llamar a toDoubleOrNull en un string vacío o solo con "."
                        val newValue = if (text.isNotEmpty() && text != ".") {
                            text.toDoubleOrNull() ?: 0.0
                        } else {
                            0.0
                        }
                        onPriceChange(newValue)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors( // 'textFieldColors' está obsoleto, se usa 'colors'
                    focusedTextColor = colors.textColor,
                    unfocusedTextColor = colors.textColor,
                    focusedContainerColor = Color.Transparent, // 'backgroundColor' obsoleto
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                placeholder = {
                    Text(
                        "0.0",
                        style = TextStyle(
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Gray
                        )
                    )
                }
            )
            Text(
                text = "USD",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Gray
            )
        }
        Divider(color = Color.Black, thickness = 2.dp)
    }
}

@Composable
private fun ExpenseTypeSelector(
    categorySelected: String,
    openBottomSheet: () -> Unit
) {
    val colors = getColorsTheme()
    Row(
        modifier = Modifier.clickable { openBottomSheet.invoke() }, // Hacer toda la fila clickable
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Expenses made for",
                fontSize = 20.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = categorySelected,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textColor
            )
        }
        IconButton(
            modifier = Modifier.clip(RoundedCornerShape(35)).background(colors.colorArrowRound),
            onClick = {
                openBottomSheet.invoke()
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select a category",
                tint = colors.textColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Necesario para TextFieldDefaults
@Composable
fun ExpenseDescription(
    descriptionContent: String,
    onDescriptionChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    var text by remember { mutableStateOf(descriptionContent) }
    val colors = getColorsTheme()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(            text = "Description",
            fontSize = 20.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text, // Intercambié value y onValueChange para el orden común
            onValueChange = { newText ->
                // --- CORRECCIÓN AQUÍ ---
                // Verifica si la nueva longitud es menor que 200.
                // Si quieres permitir hasta 200 caracteres inclusive, usa <=
                if (newText.length < 200) {
                    text = newText
                    onDescriptionChange(newText)
                }
                // Opcional: si quieres truncar el texto si excede los 200 en lugar de ignorar la entrada:
                /*
                else if (newText.length >= 200) {
                    text = newText.substring(0, 200)
                    onDescriptionChange(text)
                }
                */
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = colors.textColor,
                unfocusedTextColor = colors.textColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            placeholder = { Text("Enter a description", color = Color.Gray) }
        )
        Divider(color = Color.Black, thickness = 2.dp)
    }
}


@Composable
private fun CategoryBottomSheetContent(
    categories: List<ExpenseCategory>,
    onCategorySelected: (category: ExpenseCategory) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onCategorySelected = onCategorySelected
            )
        }
    }
}

@Composable
private fun CategoryItem(category: ExpenseCategory, onCategorySelected: (ExpenseCategory) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onCategorySelected(category)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            imageVector = category.icon,
            contentDescription = category.name,
            contentScale = ContentScale.Crop
        )
        Text(text = category.name)
    }
}
