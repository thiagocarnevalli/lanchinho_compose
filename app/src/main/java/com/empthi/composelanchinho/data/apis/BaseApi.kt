package com.empthi.composelanchinho.data.apis

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseApi(
    private val serviceUrl: String,
    private val customClient: OkHttpClient? = null
) {
    val retrofit by lazy { buildRetrofit(serviceUrl, customClient) }

    private fun buildRetrofit(serviceUrl: String, customClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(serviceUrl)
            addConverterFactory(GsonConverterFactory.create())
            customClient?.let {
                client(it)
            }
        }.build()
    }
}