package com.side.project.messenger.di

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.side.project.messenger.ui.DialogManager
import com.side.project.messenger.ui.viewModel.LaunchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (activity: Activity) -> LaunchViewModel(activity) }
}

val dialogModule = module {
    single { (activity: Activity) -> DialogManager(activity) }
}