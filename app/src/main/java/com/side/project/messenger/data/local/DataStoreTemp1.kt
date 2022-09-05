package com.side.project.messenger.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// https://juejin.cn/post/7112486451626901540

const val PDS = "pds"
val KEY_USER_NAME = stringPreferencesKey("userName")

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = PDS)

class MainViewModel : ViewModel() {

    val preLiveData = MutableLiveData<String>()

    //存储数据
    fun putValue(dataStore: DataStore<Preferences>, content: String, key: Preferences.Key<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { settings ->
                settings[key] = content
            }
        }
    }

    //获取数据
    fun getValue(dataStore: DataStore<Preferences>,key: Preferences.Key<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { settings ->
                val text = settings[key]
                preLiveData.postValue(text)
            }
        }
    }

    /**
     * 清楚所有键
     */
    fun clearPreferences(dataStore: DataStore<Preferences>){
        viewModelScope.launch {
            dataStore.edit {
                it.clear()
            }
        }
    }
}