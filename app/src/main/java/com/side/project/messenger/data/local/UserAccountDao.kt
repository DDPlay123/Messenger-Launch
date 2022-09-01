package com.side.project.messenger.data.local

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface UserAccountDao {

    @Query("SELECT * FROM userAccounts ORDER BY userId DESC")
    fun getAllAccount(): Observable<List<UserAccounts>>

    @Query("SELECT * FROM userAccounts WHERE userId = :userId")
    fun getAccountByUserId(userId: String): Observable<UserAccounts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAccount(userAccounts: UserAccounts): Completable

    @Delete
    fun deleteUserAccount(userAccounts: UserAccounts): Completable
}