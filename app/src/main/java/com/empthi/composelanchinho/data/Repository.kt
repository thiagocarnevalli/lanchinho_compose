package com.empthi.composelanchinho.data

import com.empthi.composelanchinho.data.apis.FoodApi
import com.empthi.composelanchinho.data.entities.Food
import com.empthi.composelanchinho.data.interfaces.IFoodApi


class Repository(
    private val foodApi: FoodApi
) : IFoodApi {

    override suspend fun searchByName(term: String): Result<List<Food>?> {
        return foodApi.searchByName(term = term)
    }
}