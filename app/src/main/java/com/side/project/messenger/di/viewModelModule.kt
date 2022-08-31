package com.side.project.messenger.di

import com.side.project.messenger.viewModels.LaunchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LaunchViewModel() }
}