package com.empthi.composelanchinho.domain.usecases

import com.empthi.composelanchinho.data.Repository
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.extensions.toUi

class GetFoodsUseCase(private val repository: Repository) {
    suspend fun getListOfAvailableFoods(searchTerm: String): List<FoodUI> {
        return repository.searchByName(searchTerm)?.map {
            it.toUi().apply {
                this.uri
            }
        } ?: listOf()
    }
}