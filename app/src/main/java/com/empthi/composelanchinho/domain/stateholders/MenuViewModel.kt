package com.empthi.composelanchinho.domain.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.entities.Order
import com.empthi.composelanchinho.domain.usecases.GetFoodsUseCase
import com.empthi.composelanchinho.ui.MenuListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class MenuViewModel(private val foodsUseCase: GetFoodsUseCase) : ViewModel(), MenuListener,
    KoinComponent {
    private val orders: MutableList<Order> = mutableListOf()
    private val _state: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    private val _action: MutableStateFlow<UIEvent> = MutableStateFlow(UIEvent.Initial)
    val state: StateFlow<UIState> get() = _state
    val action: StateFlow<UIEvent> get() = _action


    private fun loadMenu(term: String) {
        viewModelScope.launch {
            foodsUseCase.getListOfAvailableFoods(term).let availableFoods@{
                if (it.isNullOrEmpty()) {
                    _state.value = UIState.Error
                    return@availableFoods
                }
                /* The "/preview" is a extra path on the the meal API to return
                        a optimized image size. Ref: https://www.themealdb.com/api.php */
                _state.value = UIState.MenuLoaded(it.map { item ->
                    item.apply {
                        uri.plus("/preview")
                    }
                })
            }
        }
    }

    private fun addOrder(order: FoodUI) {
        orders.add(Order(food = order, Random.nextFloat()))
        _state.value = UIState.WaitOrders(orders.map {
            it
        })
    }

    override fun onInit(term: String) {
        loadMenu(term)
    }

    override fun onAddOrder(order: FoodUI) {
        addOrder(order)
    }


}

sealed class UIEvent {
    object Initial : UIEvent()
    data class AddOrder(val order: FoodUI) : UIEvent()
}

sealed class UIState {
    object Initial : UIState()
    object Error : UIState()
    data class MenuLoaded(val data: List<FoodUI>) : UIState()
    data class WaitOrders(val items: List<Order>) : UIState()
}
