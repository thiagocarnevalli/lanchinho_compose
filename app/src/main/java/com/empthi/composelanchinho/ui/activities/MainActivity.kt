package com.empthi.composelanchinho.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.stateholders.MenuViewModel
import com.empthi.composelanchinho.domain.stateholders.UIEvent
import com.empthi.composelanchinho.domain.stateholders.UIState
import com.empthi.composelanchinho.ui.MenuListener
import com.empthi.composelanchinho.ui.composables.FoodCardsGrid
import com.empthi.composelanchinho.ui.composables.OrdersTracker
import com.empthi.composelanchinho.ui.theme.ComposeLanchinhoTheme
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    private val menuViewModel by viewModel<MenuViewModel>()
    private val letters =
        mutableListOf("b", "c", "a", "s") //To open a difference search, just for fun :)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeLanchinhoTheme {
                MainActivityScreen(
                    menuViewModel,
                    menuViewModel.state
                )
            }
        }
        setupEventsObserver()
    }

    private fun setupEventsObserver() {
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                menuViewModel.action.collect {
                    when (it) {
                        is UIEvent.Initial -> {
                            menuViewModel.loadMenu(letters.random())
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun MainActivityScreen(
    listener: MenuListener,
    currentState: StateFlow<UIState>
) {
    //States
    val state by remember { mutableStateOf(currentState) }
    var isShowingOrders by remember { mutableStateOf(true) }
    var clientOrders by remember { mutableStateOf(listOf<FoodUI>()) }
    var menu by remember { mutableStateOf(listOf<FoodUI>()) }

    val gridSize = if (isShowingOrders && clientOrders.isNotEmpty()) 0.7f else 1f

    state.collectAsState().value.let {
        when (it) {
            is UIState.MenuLoaded -> {
                menu = it.data
            }
            is UIState.WaitOrders -> {
                clientOrders = it.items
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
                listener.onAddOrder(order)
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (clientOrders.isNotEmpty()) {
            OrdersTracker(
                modifier = Modifier.align(Alignment.BottomStart),
                showOrders = isShowingOrders,
                clientOrders = clientOrders
            ) {
                isShowingOrders = !isShowingOrders
            }
        }
    }
}

