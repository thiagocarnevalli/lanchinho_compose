package com.empthi.composelanchinho.domain.usecases

import com.empthi.composelanchinho.data.Repository
import com.empthi.composelanchinho.domain.entities.CustomException
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.extensions.toUi

class GetFoodsUseCase(private val repository: Repository) {
    suspend fun getListOfAvailableFoods(searchTerm: String): List<FoodUI> {
        return repository.searchByName(term = searchTerm).getOrNull()?.let { list ->
            return list.map { it.toUi() }
        } ?: emptyList()
    }
}