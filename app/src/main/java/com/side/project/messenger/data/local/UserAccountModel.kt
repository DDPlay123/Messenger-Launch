package com.side.project.messenger.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

data class UserAccount (

    var userImage: String,
    var userFirstName: String,
    var userLastName: String,
    var userGender: String,
    var userPhone: String,
    var userEmail: String,
    var userPassword: String

): Serializable

@Entity(tableName = "UserAccounts")
data class UserAccounts (
    @PrimaryKey
    var userId: String,

    var userAccount: UserAccount
)

class UserAccountConverters {
    @TypeConverter
    fun fromUserAccount(userAccount: UserAccount): String {
        val type = object : TypeToken<UserAccount>() {}.type
        return Gson().toJson(userAccount, type)
    }

    @TypeConverter
    fun toUserAccount(userAccountString: String): UserAccount {
        val type = object : TypeToken<UserAccount>() {}.type
        return Gson().fromJson(userAccountString, type)
    }
}