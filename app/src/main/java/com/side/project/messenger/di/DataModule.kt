package com.side.project.messenger.di

import androidx.room.Room
import com.side.project.messenger.data.local.UserAccountDb
import com.side.project.messenger.data.repo.UserAccountRepo
import com.side.project.messenger.data.repo.UserAccountRepoImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            UserAccountDb::class.java,
            UserAccountDb::class.java.simpleName
        ).fallbackToDestructiveMigration()
            .build()
    }
}

var daoModule = module {
    single { get<UserAccountDb>().userAccountDao() }
}

var repoModule = module {
    single<UserAccountRepo> { UserAccountRepoImpl() }
}