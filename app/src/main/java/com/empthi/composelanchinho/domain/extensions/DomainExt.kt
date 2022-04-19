package com.empthi.composelanchinho.domain.extensions

import com.empthi.composelanchinho.data.entities.Food
import com.empthi.composelanchinho.domain.entities.FoodUI

fun Food.toUi(): FoodUI {
    return FoodUI(
        id = this.id,
        name = this.name,
        uri = this.imageUrl
    )
}