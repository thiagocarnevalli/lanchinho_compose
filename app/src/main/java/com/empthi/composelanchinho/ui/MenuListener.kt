package com.empthi.composelanchinho.ui

import com.empthi.composelanchinho.domain.entities.FoodUI

interface MenuListener {
    fun onLoadMenu()
    fun onAddOrder(order: FoodUI)
}