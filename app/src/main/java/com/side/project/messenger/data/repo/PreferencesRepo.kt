package com.side.project.messenger.data.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

enum class UsersPreference {
    USERS_PREFERENCE, ACCOUNT_SHARE
}

interface PreferencesRepo {
    suspend fun putRecordUserAccount(userID: String)
    suspend fun getRecordUserAccount(): Flow<String>
    suspend fun clearUserPrefs()
    suspend fun clearAccountShare()
}

class PreferencesRepoImpl(private val context: Context) : PreferencesRepo, KoinComponent {
    private val Context.userPrefs: DataStore<Preferences> by preferencesDataStore(name = UsersPreference.USERS_PREFERENCE.name)
    private val Context.accountShare: DataStore<Preferences> by preferencesDataStore(name = UsersPreference.ACCOUNT_SHARE.name)

    companion object {
        // Data Share Preference
        val KEY_RECORD_USER_ACCOUNT = stringPreferencesKey("RecordUserACCOUNT")
    }

    // 單個帳戶使用的
    override suspend fun putRecordUserAccount(userID: String) {
        context.userPrefs.edit { recordUserAccount ->
            recordUserAccount[KEY_RECORD_USER_ACCOUNT] = userID
        }
    }

    override suspend fun getRecordUserAccount(): Flow<String> =
        context.userPrefs.data.map { recordUserAccount ->
            recordUserAccount[KEY_RECORD_USER_ACCOUNT] ?: ""
        }

    override suspend fun clearUserPrefs() { context.userPrefs.edit { it.clear() } }

    // 多個帳戶共用的
    override suspend fun clearAccountShare() { context.accountShare.edit { it.clear() } }
}