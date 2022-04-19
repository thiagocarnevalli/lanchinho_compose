package com.empthi.composelanchinho

import android.app.Application
import com.empthi.composelanchinho.di.appModules
import com.empthi.composelanchinho.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(networkModule, appModules)
        }
    }
}