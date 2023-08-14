package com.mivanovskaya.gittest.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyValueStorage @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences = createSharedPrefs(context)

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN_KEY, null)
        set(token) = prefs.edit().putString(AUTH_TOKEN_KEY, token).apply()

    var login: String?
        get() = prefs.getString(LOGIN_KEY, null)
        set(login) = prefs.edit().putString(LOGIN_KEY, login).apply()

    private fun createSharedPrefs(context: Context): SharedPreferences {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                SHARED_PREF_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        }
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token"
        private const val SHARED_PREF_NAME = "shared_name"
        private const val LOGIN_KEY = "login"
    }
}