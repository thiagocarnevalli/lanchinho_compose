package com.empthi.composelanchinho.data.apis

import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import retrofit2.Converter
import retrofit2.Converter.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.lang.reflect.Type


class BaseApi(
    private val serviceUrl: String,
    private val customClient: OkHttpClient? = null
) {
    val retrofit by lazy { buildRetrofit(serviceUrl, customClient) }

    private fun buildRetrofit(serviceUrl: String, customClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(serviceUrl)
            addConverterFactory(NullOnEmptyConverterFactory())
            addConverterFactory(GsonConverterFactory.create())
            customClient?.let {
                client(it)
            }
        }.build()
    }
}

internal class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any> {
        val delegate: Converter<ResponseBody, Any> =
            retrofit.nextResponseBodyConverter(this, type, annotations)
        return Converter { body ->
            //Only because the themealdb api returns a blank body  on error and the retrofit throws a exception in this scenario.
            try {
                delegate.convert(body)
            } catch (e: Exception) {
                val mockBody = ResponseBody.create(contentType = body.contentType(), content = "{}")
                delegate.convert(mockBody)
            }
        }
    }
}