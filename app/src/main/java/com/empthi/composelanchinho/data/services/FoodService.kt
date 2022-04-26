package com.empthi.composelanchinho.data.services

import com.empthi.composelanchinho.data.entities.ApiResponse
import com.empthi.composelanchinho.data.entities.Food
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface FoodService {
    @GET("{method}")
    suspend fun execute(
        @Path("method") method: String,
        @QueryMap params: Map<String, String>
    ): ApiResponse<List<Food>>
}