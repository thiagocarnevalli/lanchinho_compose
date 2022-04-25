package com.empthi.composelanchinho.domain.entities

data class FoodUI(
    val id: String,
    val name: String,
    var uri: String
)

data class Order(
    val food: FoodUI,
    val waitingTime: Float
)