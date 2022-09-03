package com.side.project.messenger.utils

import android.app.Application
import com.side.project.messenger.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class AppConfig: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AppConfig)
            modules(listOf(
                dbModule,
                daoModule,
                repoModule,
                dialogModule,
                viewModelModule
            ))
        }
    }
}