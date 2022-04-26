package com.empthi.composelanchinho.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.empthi.composelanchinho.R
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.entities.Order
import com.empthi.composelanchinho.domain.stateholders.MenuViewModel
import com.empthi.composelanchinho.domain.interfaces.MenuAction
import com.empthi.composelanchinho.domain.interfaces.MenuState
import com.empthi.composelanchinho.domain.interfaces.MenuViewListener
import com.empthi.composelanchinho.ui.composables.ErrorComponent
import com.empthi.composelanchinho.ui.composables.FoodCardsGrid
import com.empthi.composelanchinho.ui.composables.OrdersTracker
import com.empthi.composelanchinho.ui.theme.ComposeLanchinhoTheme
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity(), MenuViewListener {
    private val menuViewModel by viewModel<MenuViewModel>()
    private val letters = mutableListOf("b", "c", "a", "s") //To open a difference search, just for fun :)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLanchinhoTheme {
                MainActivityScreen(
                    listener = this,
                    refState = menuViewModel.state
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
                        is MenuAction.Action.Initialize -> {
                            menuViewModel.onInit(letters.random())
                        }
                        is MenuAction.Action.LoadMenuAction -> {
                            menuViewModel.onLoadMenu(it.term)
                        }
                        is MenuAction.Action.AddOrderAction -> {
                            menuViewModel.onAddOrder(order = it.order)
                        }
                    }
                }
            }
        }
    }

    override fun onOrderSelected(order: FoodUI) {
        menuViewModel.onOrderSelected(order = order)
    }


}

@Composable
private fun MainActivityScreen(
    listener: MenuViewListener,
    refState: StateFlow<MenuState.State>,
    modifier: Modifier = Modifier
) {
    //States
    val state by remember { mutableStateOf(refState) }
    var isShowingOrders by remember { mutableStateOf(true) }
    var clientOrders by rememberSaveable { mutableStateOf(listOf<Order>()) }
    var menu by rememberSaveable { mutableStateOf(listOf<FoodUI>()) }

    val gridSize = if (isShowingOrders && clientOrders.isNotEmpty()) 0.7f else 1f

    state.collectAsState().value.let {
        when (it) {
            is MenuState.State.MenuLoaded -> {
                menu = it.items
            }
            is MenuState.State.WaitOrders -> {
                clientOrders = it.items
            }
            is MenuState.State.Error -> {
                it.error?.let { exception ->
                    ErrorComponent(
                        message =
                        exception.message ?: stringResource(id = R.string.generic_error_message)
                    ) {
                        exception.retry.invoke()
                    }
                }
            }
            else -> {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Surface(
            modifier = modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight(gridSize)
        ) {
            FoodCardsGrid(list = menu) { order ->
                listener.onOrderSelected(order)
            }
        }
        Spacer(modifier = modifier.size(8.dp))
        if (clientOrders.isNotEmpty()) {
            OrdersTracker(
                modifier = modifier.align(Alignment.BottomStart),
                showOrders = isShowingOrders,
                clientOrders = clientOrders
            ) {
                isShowingOrders = !isShowingOrders
            }
        }
    }
}

