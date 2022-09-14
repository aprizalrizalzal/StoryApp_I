package me.zal.rizal.aprizal.storyapp.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.zal.rizal.aprizal.storyapp.model.users.UserModel

class UsersPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[STATE_KEY] ?: false,
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[USER_ID_KEY] ?: ""
            )
        }
    }

    suspend fun saveSignIn(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = userModel.isLogin
            preferences[NAME_KEY] = userModel.name
            preferences[TOKEN_KEY] = userModel.token
            preferences[USER_ID_KEY] = userModel.userId
        }
    }

    suspend fun signOut() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UsersPreference? = null

        private val STATE_KEY = booleanPreferencesKey("state")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID_KEY = stringPreferencesKey("userId")

        fun getInstance(dataStore: DataStore<Preferences>): UsersPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UsersPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}