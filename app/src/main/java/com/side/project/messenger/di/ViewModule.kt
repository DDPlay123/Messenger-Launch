package com.side.project.messenger.di

import android.app.Activity
import com.side.project.messenger.ui.DialogManager
import com.side.project.messenger.ui.vm.LaunchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LaunchViewModel() }
}

val dialogModule = module {
    single { (activity: Activity) -> DialogManager(activity) }
}