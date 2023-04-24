package com.mivanovskaya.gittest.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

const val AUTH_TOKEN = "auth_token"
const val TOKEN_SHARED_NAME = "token_shared_pref"
const val TOKEN_ENABLED_KEY = "token_enabled"

@Singleton
class KeyValueStorage @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN, "")
        set(token) {
            editor.putString(AUTH_TOKEN, token).apply()
            authTokenEnabled = true
            Log.i("BRED", "KVS: authTkn = $authToken & authTknEnabl = $authTokenEnabled")
        }

    var authTokenEnabled: Boolean
        get() = prefs.getBoolean(TOKEN_ENABLED_KEY, false)
        set(tokenEnabled) = editor.putBoolean(TOKEN_ENABLED_KEY, tokenEnabled).apply()

    private fun createEncryptedSharedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            TOKEN_SHARED_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    init {
        prefs = createEncryptedSharedPrefs(context)
        editor = prefs.edit()
    }
}