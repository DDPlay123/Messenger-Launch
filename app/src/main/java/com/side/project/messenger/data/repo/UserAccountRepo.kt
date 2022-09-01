package com.side.project.messenger.data.repo


import com.side.project.messenger.data.local.UserAccountDao
import com.side.project.messenger.data.local.UserAccounts
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserAccountRepo {
    fun getAllAccount(): Observable<List<UserAccounts>>
    fun getAccountByUserId(userId: String): Observable<UserAccounts>
    fun insertUserAccount(userAccounts: UserAccounts): Completable
    fun deleteUserAccount(userAccounts: UserAccounts): Completable
}

class UserAccountRepoImpl: UserAccountRepo, KoinComponent {
    private val userAccountDao: UserAccountDao by inject()

    override fun getAllAccount(): Observable<List<UserAccounts>> =
        userAccountDao.getAllAccount()

    override fun getAccountByUserId(userId: String): Observable<UserAccounts> =
        userAccountDao.getAccountByUserId(userId)

    override fun insertUserAccount(userAccounts: UserAccounts) =
        userAccountDao.insertUserAccount(userAccounts)

    override fun deleteUserAccount(userAccounts: UserAccounts) =
        userAccountDao.deleteUserAccount(userAccounts)
}