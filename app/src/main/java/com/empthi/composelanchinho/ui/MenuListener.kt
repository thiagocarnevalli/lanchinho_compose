package com.empthi.composelanchinho.ui

import com.empthi.composelanchinho.domain.entities.FoodUI

interface MenuListener {
    fun onInit(term: String)
    fun onAddOrder(order: FoodUI)
}