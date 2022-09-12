package me.zal.rizal.aprizal.storyapp.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.zal.rizal.aprizal.storyapp.model.LoginResult
import me.zal.rizal.aprizal.storyapp.model.users.UserModel

class UsersPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[STATE_KEY] ?: false,
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[USER_ID_KEY] ?: ""
            )
        }
    }

    suspend fun saveSignUp(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = userModel.name
            preferences[EMAIL_KEY] = userModel.email
            preferences[PASSWORD_KEY] = userModel.password
        }
    }

    suspend fun saveSignIn(loginResult: LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = loginResult.name
            preferences[TOKEN_KEY] = loginResult.token
            preferences[USER_ID_KEY] = loginResult.userId
        }
    }

    suspend fun signIn() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun signOut() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[EMAIL_KEY] = ""
            preferences[PASSWORD_KEY] = ""
            preferences[NAME_KEY] = ""
            preferences[TOKEN_KEY] = ""
            preferences[USER_ID_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UsersPreference? = null

        private val STATE_KEY = booleanPreferencesKey("state")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
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