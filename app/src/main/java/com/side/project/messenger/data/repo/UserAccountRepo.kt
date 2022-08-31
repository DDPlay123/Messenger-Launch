package com.side.project.messenger.data.repo


import com.side.project.messenger.data.local.UserAccountDao
import com.side.project.messenger.data.local.UserAccounts
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserAccountRepo {
    suspend fun getAllAccount(): Observable<List<UserAccounts>>
    suspend fun getAccountByEmail(email: String): Observable<UserAccounts>
    suspend fun insertUserAccount(userAccounts: UserAccounts)
    suspend fun deleteUserAccount(userAccounts: UserAccounts)
}

class UserAccountRepoImpl: UserAccountRepo, KoinComponent {
    private val account: UserAccountDao by inject()

    override suspend fun getAllAccount(): Observable<List<UserAccounts>> =
        account.getAllAccount()


    override suspend fun getAccountByEmail(email: String): Observable<UserAccounts> =
        account.getAccountByEmail(email)

    override suspend fun insertUserAccount(userAccounts: UserAccounts) =
        account.insertUserAccount(userAccounts)

    override suspend fun deleteUserAccount(userAccounts: UserAccounts) =
        account.deleteUserAccount(userAccounts)
}