package com.side.project.messenger.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserAccounts::class], version = 1, exportSchema = false)
@TypeConverters(UserAccountConverters::class)
abstract class UserAccountDb: RoomDatabase() {
    abstract fun userAccountDao(): UserAccountDao
}