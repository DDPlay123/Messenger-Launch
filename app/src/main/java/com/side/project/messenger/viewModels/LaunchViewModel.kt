package com.side.project.messenger.viewModels


import androidx.lifecycle.ViewModel
import com.side.project.messenger.data.local.UserAccounts
import com.side.project.messenger.data.repo.UserAccountRepo
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LaunchViewModel: ViewModel(), KoinComponent {
    private val repo: UserAccountRepo by inject()

    suspend fun getAllAccount(): Observable<List<UserAccounts>> =
        repo.getAllAccount()

    suspend fun getAccountByEmail(email: String): Observable<UserAccounts> =
        repo.getAccountByEmail(email)

    suspend fun insertUserAccount(userAccounts: UserAccounts) =
        repo.insertUserAccount(userAccounts)

    suspend fun deleteUserAccount(userAccounts: UserAccounts) =
        repo.deleteUserAccount(userAccounts)
}