package com.empthi.composelanchinho.di

import com.empthi.composelanchinho.data.Repository
import com.empthi.composelanchinho.data.apis.BaseApi
import com.empthi.composelanchinho.data.apis.FoodApi
import com.empthi.composelanchinho.domain.stateholders.MainViewModel
import com.empthi.composelanchinho.domain.usecases.GetFoodsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModules = module {
    viewModel { MainViewModel(get()) }
}

val networkModule = module {
    factory { GetFoodsUseCase(get()) }
    single { Repository(get()) }
    single { provideFoodApi(get()) }
    factory { provideBaseApiWithoutOkHttp() }
}




fun provideFoodApi(baseApi: BaseApi): FoodApi {
    return FoodApi(baseApi)
}

fun provideBaseApiWithoutOkHttp(): BaseApi {
    return BaseApi("https://www.themealdb.com/api/json/v1/1/")
}