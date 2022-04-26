package com.empthi.composelanchinho.domain.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.entities.Order
import com.empthi.composelanchinho.domain.interfaces.*
import com.empthi.composelanchinho.domain.usecases.GetFoodsUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class MenuViewModel(private val foodsUseCase: GetFoodsUseCase) : ViewModel(),
    MenuState by MenuStateImpl(),
    MenuAction by MenuActionImpl(),
    MenuListener,
    KoinComponent {
    private val orders: MutableList<Order> = mutableListOf()

    init {
        handleInitialSetup()
    }

    override fun onInit(term: String) {
        handleLoadMenu(term)
    }

    override fun onLoadMenu(term: String) {
        loadMenu(term)
    }

    override fun onAddOrder(order: FoodUI) {
        addOrder(order)
    }

    override fun onOrderSelected(order: FoodUI) {
        handleAddOrder(order = order)
    }

    private fun addOrder(order: FoodUI) {
        orders.add(Order(food = order, Random.nextFloat()))
        toWaitOrdersState(orders.map {
            it
        })
    }

    private fun loadMenu(term: String) {
        viewModelScope.launch {
            foodsUseCase.getListOfAvailableFoods(term).let availableFoods@{
                if (it.isNullOrEmpty()) {
                    toErrorState("Ops! Algo deu errado") { onLoadMenu(term) }
                    return@availableFoods
                }
                /* The "/preview" is a extra path on the the meal API to return
                        a optimized image size. Ref: https://www.themealdb.com/api.php */
                toMenuLoadedState(it.map { item -> item.apply { uri.plus("/preview") } })
            }
        }
    }
}


