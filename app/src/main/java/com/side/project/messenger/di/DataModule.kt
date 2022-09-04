package com.side.project.messenger.di

import android.app.Activity
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.side.project.messenger.data.local.UserAccountDb
import com.side.project.messenger.data.repo.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val firebaseModel = module {
    factory { (activity: Activity) ->
        PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
    }
    single { FirebaseFirestore.getInstance() }
}

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
    single<FirebaseAuthRepo> { (activity: Activity) -> FirebaseAuthRepoImpl(activity) }
    single<FirebaseStoreRepo> { FirebaseStoreRepoImpl() }
}