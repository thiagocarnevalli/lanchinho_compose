package com.empthi.composelanchinho.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.stateholders.MainViewModel
import com.empthi.composelanchinho.domain.stateholders.UIEvent
import com.empthi.composelanchinho.domain.stateholders.UIState
import com.empthi.composelanchinho.ui.composables.FoodCardsGrid
import com.empthi.composelanchinho.ui.composables.OrdersTracker
import com.empthi.composelanchinho.ui.theme.ComposeLanchinhoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    val mainViewModel  by viewModel<MainViewModel>()
    private val letters =
        mutableListOf("b", "c", "a", "s") //To open a difference search, just for fun :)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeLanchinhoTheme {
                MainActivityScreen(mainViewModel, letters.random())
            }
        }
    }
}

@Composable
private fun MainActivityScreen(mainViewModel: MainViewModel, randomLetter: String) {
    //States
    val uiState by remember { mutableStateOf(mainViewModel.state) }
    val uiEvent by remember { mutableStateOf(mainViewModel.action) }
    var isShowingOrders by remember { mutableStateOf(false) }
    var clientOrders by remember { mutableStateOf(listOf<FoodUI>()) }
    var menu by remember { mutableStateOf(listOf<FoodUI>()) }

    val gridSize = if (isShowingOrders) 0.7f else 1f

    uiEvent.collectAsState().let { action ->
        when (action.value) {
            is UIEvent.Initial -> {
                mainViewModel.loadMenu(randomLetter)
            }
            is UIEvent.AddOrder -> {
                val order = (action.value as UIEvent.AddOrder).order
                mainViewModel.addOrder(order)
            }
        }
    }

    uiState.collectAsState().let { state ->
        when (state.value) {
            is UIState.MenuLoaded -> {
                menu = (state.value as UIState.MenuLoaded).data
            }
            is UIState.WaitOrders -> {
                clientOrders = (state.value as UIState.WaitOrders).items
                isShowingOrders = true
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight(gridSize)
        ) {
            FoodCardsGrid(list = menu) { order ->
                mainViewModel.addOrder(order = order)
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (clientOrders.isNotEmpty()) {
            OrdersTracker(
                modifier = Modifier.align(Alignment.BottomStart),
                showOrders = isShowingOrders,
                clientOrders = clientOrders
            ) {
                isShowingOrders = it
            }
        }
    }
}

