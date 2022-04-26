package com.empthi.composelanchinho.data.interfaces

import com.empthi.composelanchinho.data.entities.Food

interface IFoodApi {
    suspend fun searchByName(term: String): Result<List<Food>?>
}