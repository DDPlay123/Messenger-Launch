package com.side.project.messenger.utils

import android.app.Application
import com.side.project.messenger.di.daoModule
import com.side.project.messenger.di.dbModule
import com.side.project.messenger.di.repoModule
import com.side.project.messenger.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class AppConfig: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(
                dbModule,
                daoModule,
                repoModule,
                viewModelModule
            ))
        }
    }
}