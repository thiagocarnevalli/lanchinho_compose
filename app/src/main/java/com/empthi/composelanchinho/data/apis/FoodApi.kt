package com.empthi.composelanchinho.data.apis

import com.empthi.composelanchinho.data.entities.Food
import com.empthi.composelanchinho.data.interfaces.IFoodApi
import com.empthi.composelanchinho.data.services.FoodService


class FoodApi(val base: BaseApi) : IFoodApi {
    private val service by lazy { base.retrofit.create(FoodService::class.java) }

    override suspend fun searchByName(term: String): List<Food>? {
        return service.execute(
            method = "search.php",
            mapOf(
                "f" to term
            )
        ).data
    }
}