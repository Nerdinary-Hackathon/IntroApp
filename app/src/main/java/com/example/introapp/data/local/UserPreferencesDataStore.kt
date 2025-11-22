package com.example.introapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore 인스턴스 생성
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * 사용자 데이터를 DataStore에 저장하고 읽는 클래스
 */
@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val USER_ID_KEY = intPreferencesKey("user_id")
    }

    /**
     * userId 저장
     */
    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    /**
     * userId 읽기 (Flow)
     */
    fun getUserId(): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    /**
     * userId 삭제 (로그아웃 시 사용)
     */
    suspend fun clearUserId() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }

    /**
     * 모든 데이터 삭제
     */
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}