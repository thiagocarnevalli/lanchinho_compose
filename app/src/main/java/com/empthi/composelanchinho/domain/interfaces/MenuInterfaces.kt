package com.empthi.composelanchinho.domain.interfaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empthi.composelanchinho.domain.entities.CustomException
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.entities.Order
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


interface MenuViewListener {
    fun onOrderSelected(order: FoodUI)
}

interface MenuListener {
    fun onInit(term: String)
    fun onLoadMenu(term: String)
    fun onAddOrder(order: FoodUI)
    fun onOrderSelected(order: FoodUI)
}

interface MenuAction {
    val action: MutableSharedFlow<Action>

    sealed class Action {
        object Initialize : Action()
        data class LoadMenuAction(val term: String) : Action()
        data class AddOrderAction(val order: FoodUI) : Action()
    }

    fun ViewModel.handleLoadMenu(term: String) = viewModelScope.launch {
        action.emit(Action.LoadMenuAction(term))
    }

    fun ViewModel.handleAddOrder(order: FoodUI) = viewModelScope.launch {
        action.emit(Action.AddOrderAction(order))
    }

    fun ViewModel.handleInitialSetup() = viewModelScope.launch {
        action.emit(Action.Initialize)
    }
}

interface MenuState {
    val state: MutableStateFlow<State>

    sealed class State {
        object Initial : State()
        data class Error(val error: CustomException?) : State()
        data class MenuLoaded(val items: List<FoodUI>) : State()
        data class WaitOrders(val items: List<Order>) : State()
    }

    fun MenuState.toInitialState() {
        state.value = State.Initial
    }

    fun MenuState.toErrorState(errorMessage: String, onRetry: () -> Unit) {
        state.value = State.Error(CustomException(errorMessage, retry = onRetry))
    }

    fun MenuState.toMenuLoadedState(items: List<FoodUI>) {
        state.value = State.MenuLoaded(items)
    }

    fun MenuState.toWaitOrdersState(items: List<Order>) {
        state.value = State.WaitOrders(items)
    }
}