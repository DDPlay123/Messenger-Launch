package com.side.project.messenger.viewModels


import androidx.lifecycle.ViewModel
import com.side.project.messenger.data.local.UserAccounts
import com.side.project.messenger.data.repo.UserAccountRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LaunchViewModel: ViewModel(), KoinComponent {
    private val userAccountRepo: UserAccountRepo by inject()

    fun getAllAccount(): Observable<List<UserAccounts>> =
        userAccountRepo.getAllAccount()

    fun getAccountByUserId(userId: String): Observable<UserAccounts> =
        userAccountRepo.getAccountByUserId(userId)

    fun insertUserAccount(userAccounts: UserAccounts): Completable =
        userAccountRepo.insertUserAccount(userAccounts)

    fun deleteUserAccount(userAccounts: UserAccounts): Completable =
        userAccountRepo.deleteUserAccount(userAccounts)
}