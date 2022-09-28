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

/***
    Preference DataStore Template-1
    參考用，無實際使用。
    參考：https://juejin.cn/post/7112486451626901540
 ***/

// String KEY Name
val KEY_USER_NAME = stringPreferencesKey("userName")
// DataStore Point
val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "NAME")

class MainViewModel : ViewModel() {

    private val preLiveData = MutableLiveData<String>()

    // Storage String Data
    fun putValue(dataStore: DataStore<Preferences>, content: String, key: Preferences.Key<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { settings ->
                settings[key] = content
            }
        }
    }

    // Obtain String Data
    fun getValue(dataStore: DataStore<Preferences>,key: Preferences.Key<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { settings ->
                val text = settings[key]
                preLiveData.postValue(text)
            }
        }
    }

    // Clear All DataStore
    fun clearPreferences(dataStore: DataStore<Preferences>){
        viewModelScope.launch {
            dataStore.edit {
                it.clear()
            }
        }
    }
}