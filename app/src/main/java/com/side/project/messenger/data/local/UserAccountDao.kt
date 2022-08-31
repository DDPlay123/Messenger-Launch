package com.side.project.messenger.data.local


import androidx.room.*
import io.reactivex.rxjava3.core.Observable

@Dao
interface UserAccountDao {

    @Query("SELECT * FROM userAccounts ORDER BY email DESC")
    fun getAllAccount(): Observable<List<UserAccounts>>

    @Query("SELECT * FROM userAccounts WHERE email = :email")
    fun getAccountByEmail(email: String): Observable<UserAccounts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAccount(userAccounts: UserAccounts)

    @Delete
    fun deleteUserAccount(userAccounts: UserAccounts)
}