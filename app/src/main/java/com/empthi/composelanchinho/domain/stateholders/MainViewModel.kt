package com.empthi.composelanchinho.domain.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.usecases.GetFoodsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel( private val foodsUseCase: GetFoodsUseCase) : ViewModel(), KoinComponent {
    //private val foodsUseCase: GetFoodsUseCase by inject()
    private val orders: MutableList<FoodUI> = mutableListOf()
    private val _state: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    private val _action: MutableStateFlow<UIEvent> = MutableStateFlow(UIEvent.Initial)
    val state: StateFlow<UIState> get() = _state
    val action: StateFlow<UIEvent> get() = _action

    fun loadMenu(term: String) {
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

    fun addOrder(order: FoodUI) {
        orders.add(order)
        _state.value = UIState.WaitOrders(orders)
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
    data class WaitOrders(val items: List<FoodUI>) : UIState()
}
