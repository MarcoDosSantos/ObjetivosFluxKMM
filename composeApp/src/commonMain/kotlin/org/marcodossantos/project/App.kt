package org.marcodossantos.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.marcodossantos.project.common.AppTheme
import org.marcodossantos.project.common.getColorsTheme
import org.marcodossantos.project.data.TitleTopBarTypes
import moe.tlaster.precompose.navigation.path
import org.marcodossantos.project.navigation.Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun App() {
    PreComposeApp {
        val colors = getColorsTheme()

        AppTheme {
            val navigator = rememberNavigator()
            val titleTopBar = getTitleTopAppBar(navigator)
            val isEditOrAddExpenses = titleTopBar != TitleTopBarTypes.DASHBOARD.value
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = titleTopBar,
                                fontSize = 25.sp,
                                color = colors.textColor
                            )
                        },
                        navigationIcon = {
                            if (isEditOrAddExpenses) {
                                IconButton(
                                    onClick = {
                                        navigator.popBackStack()
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier.padding(start = 16.dp),
                                        imageVector = Icons.Default.ArrowBack,
                                        tint = colors.textColor,
                                        contentDescription = "Volver a pantalla anterior"
                                    )
                                }
                            } else {
                                Icon(
                                    modifier = Modifier.padding(start = 16.dp),
                                    imageVector = Icons.Default.Apps,
                                    tint = colors.textColor,
                                    contentDescription = "Pantalla principal"
                                )
                            }


                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.backgroundColor)
                    )
                },
                floatingActionButton = {
                    if (!isEditOrAddExpenses) {
                        FloatingActionButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                navigator.navigate("/addExpenses")
                            },
                            shape = RoundedCornerShape(50),
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ) { Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "FAB Add") }
                    }
                }

            ) { innerPadding ->
                Navigation(
                    navigator = navigator,
                    modifier = Modifier.padding(innerPadding))
            }
        }
    }

}

@Composable
fun getTitleTopAppBar(navigator: Navigator): String {
    val currentEntry by navigator.currentEntry.collectAsStateWithLifecycle(null)
    val route = currentEntry?.route?.route ?: ""
    val id = currentEntry?.path<Long>("id")

    val titleTopBar = when {
        route.startsWith("/addExpenses") && id != null -> TitleTopBarTypes.EDIT
        route.startsWith("/addExpenses") -> TitleTopBarTypes.ADD
        else -> TitleTopBarTypes.DASHBOARD
    }
    return titleTopBar.value
}


